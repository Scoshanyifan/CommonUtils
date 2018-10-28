package com.scosyf.utils.net;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.scosyf.utils.net.client.HttpConnectionClient;
import com.scosyf.utils.net.client.HttpsConnectionClient;
import com.scosyf.utils.net.common.AnyX509TrustManager;


public class HttpsTest {

    public static void main(String[] args) {
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
