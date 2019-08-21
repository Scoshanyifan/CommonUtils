package com.scosyf.utils.net.client;

import com.scosyf.utils.net.common.NetWorkConstant;
import com.scosyf.utils.net.common.NetWorkTimeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;


class NetWorkExecutor {

    private static Logger log = LoggerFactory.getLogger(NetWorkExecutor.class);

    /**
     * 
     * @param requestUrl
     * @param requestContent 请求内容，如果是GET需拼接
     * @param httpMethod GET / post
     * @param requestHeaders
     * @param timeout
     * @param ssl
     * @return
     */
    public static String executeUrlConnection(String requestUrl, String requestContent, String httpMethod,
            Map<String, String> requestHeaders, NetWorkTimeout timeout, SSLSocketFactory ssl) {
        httpMethod = httpMethod.toUpperCase();
        // 回应内容
        StringBuilder body = new StringBuilder();
        // 输出给外界服务端
        OutputStream os = null;
        // 输入流
        InputStream is = null;
        BufferedReader reader = null;

        // 设置GET请求时的requestContent
        if (NetWorkConstant.HTTP_METHOD_GET.equals(httpMethod)) {
            if (requestContent != null) {
                if (requestUrl.indexOf('?') != -1) {
                    requestUrl = requestUrl + "&" + requestContent;
                } else {
                    requestUrl = requestUrl + "?" + requestContent;
                }
            }
        }

        try {
            // 设置连接方式
            URL url = new URL(requestUrl);
            HttpURLConnection urlConn = null;
            if (ssl != null) {
                urlConn = (HttpsURLConnection) url.openConnection();
                ((HttpsURLConnection) urlConn).setSSLSocketFactory(ssl);
            } else {
                urlConn = (HttpURLConnection) url.openConnection();
            }
            // 设置超时时间
            if (timeout != null) {
                urlConn.setConnectTimeout(timeout.getConnectTimeout());
                urlConn.setReadTimeout(timeout.getReadTimeout());
            } else {
                urlConn.setConnectTimeout(NetWorkConstant.DEFAULT_CONNECT_TIMEOUT);
                urlConn.setReadTimeout(NetWorkConstant.DEFAULT_READ_TIMEOUT);
            }
            // 设置其他参数
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod(httpMethod);
            urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 设置额外的头信息
            if (requestHeaders != null) {
                for (Entry<String, String> header : requestHeaders.entrySet()) {
                    urlConn.setRequestProperty(header.getKey(), header.getValue());
                }
            }
            // 建立POST连接
            if (NetWorkConstant.HTTP_METHOD_POST.equals(httpMethod)) {
                if (requestContent != null) {
                    os = urlConn.getOutputStream();
                    os.write(requestContent.getBytes(Charset.forName("UTF-8")));
                    os.close();
                }
            } else {
                // GET
                urlConn.connect();
            }
            // 获取响应
            int responseCode = urlConn.getResponseCode();
            if (HttpURLConnection.HTTP_OK == responseCode) {
                is = urlConn.getInputStream();
                reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            } else {
                log.error(">>> url connect failure: response msg:[{}], requestUrl:[{}]", 
                        urlConn.getResponseMessage(), requestUrl);
            }
        } catch (Exception e) {
            log.error(">>> http connect error: " + e);
            return null;
        } finally {
            try {
                if (os != null) 
                    os.close();
                if (is != null) 
                    is.close();
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                log.error(">>> http connect dispose resource error: " + e);
            }
        }
        return body.toString();
    }

    public static void main(String[] args) {
        String res = NetWorkExecutor.executeUrlConnection("http://www.baidu.com", null, "get", null, null, null);
        System.out.println(res);
    }
    
}
