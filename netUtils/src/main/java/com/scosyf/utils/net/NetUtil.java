package com.scosyf.utils.net;

import com.scosyf.utils.net.common.AnyX509TrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: KunBu
 * @time: 2019/6/14 13:33
 * @description:
 */
public class NetUtil {

    private static final Logger logger = LoggerFactory.getLogger(NetUtil.class);

    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";

    public static final int DEFAULT_CONNECT_TIMEOUT = 3000;
    public static final int DEFAULT_READ_TIMEOUT = 3000;

    /**
     * @param requestUrl     请求域名
     * @param params         参数内容
     * @param httpMethod     请求方式 {@link #HTTP_METHOD_GET} {@link #HTTP_METHOD_POST}
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @return
     */
    public static Map<String, Object> doRequest(
            String requestUrl, Map<String, Object> params, String httpMethod, Integer connectTimeout, Integer readTimeout) {
        return doRequest(requestUrl, params, null, httpMethod, connectTimeout, readTimeout, null);
    }

    /**
     * @param requestUrl     请求域名
     * @param params         参数内容
     * @param httpMethod     请求方式 {@link #HTTP_METHOD_GET} {@link #HTTP_METHOD_POST}
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @return
     */
    public static Map<String, Object> doRequestWithDefaultSSL(
            String requestUrl, Map<String, Object> params, String httpMethod, Integer connectTimeout, Integer readTimeout) {

        SSLSocketFactory ssf = null;
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new AnyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从SSLContext对象中得到SSLSocketFactory对象
            ssf = sslContext.getSocketFactory();
        } catch (Exception e) {
            logger.error("获取SSLContext实例时发生异常！", e);
        }
        return doRequest(requestUrl, params, null, httpMethod, connectTimeout, readTimeout, ssf);
    }

    /**
     * @param requestUrl     请求域名
     * @param params         参数内容
     * @param header         请求头
     * @param httpMethod     请求方式 {@link #HTTP_METHOD_GET} {@link #HTTP_METHOD_POST}
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @param ssf            ssl工厂类
     * @return
     */
    public static Map<String, Object> doRequest(
            String requestUrl,
            Map<String, Object> params,
            Map<String, String> header,
            String httpMethod,
            Integer connectTimeout,
            Integer readTimeout,
            SSLSocketFactory ssf) {
        logger.info(">>> http request, url:{}, method:{}, connTimeout:{}, readTimeout:{}, ssf:{}, params:{}",
                new Object[]{requestUrl, httpMethod, connectTimeout, readTimeout, ssf, params});

        Map<String, Object> result = new HashMap<String, Object>();

        OutputStream out = null;
        InputStream in = null;
        BufferedReader bufferedReader = null;

        try {
            //构建参数内容
            String queryString = null;
            if (params != null && !params.isEmpty()) {
                StringBuilder queryBody = new StringBuilder();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    queryBody.append(entry.getKey())
                            .append("=")
                            //内容需要url encode
                            .append(URLEncoder.encode(entry.getValue().toString(), "UTF-8"))
                            .append("&");
                }
                queryString = queryBody.deleteCharAt(queryBody.length() - 1).toString();
            }

            // GET请求url和请求参数进行拼接
            if (HTTP_METHOD_GET.equalsIgnoreCase(httpMethod)) {
                if (queryString != null && queryString.length() > 0) {
                    requestUrl = requestUrl + "?" + queryString;
                }
            }

            // 生成http连接对象，并设置http请求属性
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            // 设置为https加密
            if (ssf != null) {
                ((HttpsURLConnection) httpUrlConnection).setSSLSocketFactory(ssf);
            }
            // 设置是否向httpUrlConnection输出，post请求，参数要放在http正文内，因此需要设为true，默认情况下是false;
            httpUrlConnection.setDoOutput(true);
            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpUrlConnection.setDoInput(true);
            // 忽略缓存
            httpUrlConnection.setUseCaches(false);
            // 设定请求方式
            httpUrlConnection.setRequestMethod(httpMethod);
            // 超时时间设置
            httpUrlConnection.setConnectTimeout(connectTimeout != null ? connectTimeout : DEFAULT_CONNECT_TIMEOUT);
            httpUrlConnection.setReadTimeout(readTimeout != null ? readTimeout : DEFAULT_READ_TIMEOUT);

            //设置header
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    httpUrlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (HTTP_METHOD_GET.equalsIgnoreCase(httpMethod)) {
                // GET方式访问
                httpUrlConnection.connect();
            } else {
                // POST方式需要建立流，向指向的URL传入参数
                if (queryString.length() > 0) {
                    out = httpUrlConnection.getOutputStream();
                    // 设置编码格式，防止中文乱码
                    out.write(queryString.getBytes("UTF-8"));
                    out.flush();
                    out.close();
                }
            }

            // 处理响应
            int responseCode = httpUrlConnection.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                // 将返回的输入流转换成字符串
                in = httpUrlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));

                // 应答body内容
                StringBuilder bodyBuilder = new StringBuilder();
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    bodyBuilder.append(str);
                }

                result.put("success", true);
                result.put("data", bodyBuilder.toString());
                result.put("code", 200);
                result.put("msg", "请求成功");
            } else {
                result.put("success", false);
                result.put("code", responseCode);
                result.put("msg", "请求异常");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("code", 500);
            result.put("msg", "请求异常，异常信息：" + e.getClass() + "->" + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            closeResource(result, in);
            closeResource(result, out);
            closeResource(result, bufferedReader);
        }
        return result;
    }

    private static void closeResource(Map<String, Object> result, Closeable resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                result.put("success", false);
                result.put("code", 500);
                result.put("msg", "请求异常，异常信息：" + e.getClass() + "->" + e.getMessage());
            }
        }
    }
}
