package com.scosyf.utils.excel;

import com.google.common.collect.Maps;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map;

/**
 * @program: commonUtils
 * @description: 签名工具类
 * @author: kunbu
 * @create: 2019-08-20 17:51
 **/
public class SignUtil {

    private static final String CHARSET_UTF8 = "utf-8";
    private static final String SIGN_METHOD_HMAC = "hmac";
    private static final String SIGN_METHOD_MD5 = "md5";

    private static final String AppSecret = "d62df5175c26f02f9cb6fbc126d02b28";

    public static void main(String[] args) {
        Map<String, String> params = Maps.newHashMap();
        params.put("method", "alibaba.ailabs.iot.device.list.update.notify");
        params.put("app_key", "27746755");
        params.put("sign_method", "hmac");
        params.put("timestamp", "2019-08-20 18:05:00");
        params.put("v", "2.0");
        params.put("token", "e2733a8f078f1cddfc1152188b225619");
        params.put("skill_id", "37843");

        try {
            String sign = signTopRequest(params, AppSecret, SIGN_METHOD_HMAC);
            System.out.println(sign);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String signTopRequest(Map<String, String> params, String secret, String signMethod) throws IOException {
        // 第一步：检查参数是否已经排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);

        // 第二步：把所有参数名和参数值串在一起
        StringBuilder query = new StringBuilder();
        if (SIGN_METHOD_MD5.equals(signMethod)) {
            query.append(secret);
        }
        for (String key : keys) {
            String value = params.get(key);
            query.append(key).append(value);
        }

        // 第三步：使用MD5/HMAC加密
        byte[] bytes;
        if (SIGN_METHOD_HMAC.equals(signMethod)) {
            bytes = encryptHMAC(query.toString(), secret);
        } else {
            query.append(secret);
            bytes = encryptMD5(query.toString());
        }

        // 第四步：把二进制转化为大写的十六进制(正确签名应该为32大写字符串，此方法需要时使用)
        return byte2hex(bytes);
    }

    public static byte[] encryptHMAC(String data, String secret) throws IOException {
        byte[] bytes;
        try {
            SecretKey secretKey = new SecretKeySpec(secret.getBytes(CHARSET_UTF8), "HmacMD5");
            Mac mac = Mac.getInstance(secretKey.getAlgorithm());
            mac.init(secretKey);
            bytes = mac.doFinal(data.getBytes(CHARSET_UTF8));
        } catch (GeneralSecurityException gse) {
            throw new IOException(gse.toString());
        }
        return bytes;
    }

    public static byte[] encryptMD5(String data) throws IOException {
        return null;
//        return encryptMD5(data.getBytes(CHARSET_UTF8));
    }

    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

}
