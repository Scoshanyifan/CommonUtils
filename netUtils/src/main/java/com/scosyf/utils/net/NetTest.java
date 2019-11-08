package com.scosyf.utils.net;

import com.scosyf.utils.net.client.HttpConnectionClient;
import com.scosyf.utils.net.client.HttpsConnectionClient;
import com.scosyf.utils.net.common.AnyX509TrustManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;


public class NetTest {

    public static void main(String[] args) {

        testNetUtil();
//        testHttpConnectionClient();
    }

    public static void testNetUtil() {
        System.out.println(NetUtil.doRequestWithDefaultSSL(
                "https://www.baidu.com",
                null,
                "GET",
                0,
                0));

        Map<String, Object> json = new HashMap<String, Object>();
        json.put("snStr", "xxx");
        json.put("terminalCode", "xxx");
        Map<String, String> header = new HashMap<String, String>();
        json.put("token", "5B4D1AE106A6C3A9072B4B99ADC32328");
        System.out.println(NetUtil.doRequest(
                "http://lunar-test.yunext.com/api/home/pda/good/checkrandom/batch",
                json,
                header,
                NetUtil.HTTP_METHOD_POST,
                0,
                0,
                null));
    }

    public static void testHttpConnectionClient() {
        SSLContext context = null;
        TrustManager[] tm = {new AnyX509TrustManager()};
        try {
            context = SSLContext.getInstance("SSL", "SunJSSE");
            context.init(null, tm, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        String res = HttpsConnectionClient.doRequest(context, "https://www.baidu.com", "get", null);
        System.out.println(res);

        res = HttpConnectionClient.doGetRequest("https://www.baidu.com");
        System.out.println(res);
    }

}
