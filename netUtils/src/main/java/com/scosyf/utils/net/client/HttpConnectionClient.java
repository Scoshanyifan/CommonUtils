package com.scosyf.utils.net.client;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scosyf.utils.net.common.NetWorkConstant;
import com.scosyf.utils.net.common.NetWorkTimeout;


public class HttpConnectionClient {

    private static Logger log = LoggerFactory.getLogger(HttpConnectionClient.class);

    public static String doGetRequest(String requestUrl) {
        return doGetRequest(requestUrl, null);
    }

    public static String doGetRequest(String requestUrl, Map<String, String> requestHeaders) {
        return doGetRequest(requestUrl, requestHeaders, null);
    }

    public static String doGetRequest(String requestUrl, Map<String, String> requestHeaders, NetWorkTimeout timeout) {
        return doRequest(requestUrl, NetWorkConstant.HTTP_METHOD_GET, null, requestHeaders, timeout);
    }

    public static String doPostRequest(String requestUrl, Map<String, String> requestContent) {
        return doPostRequest(requestUrl, requestContent, null);
    }

    public static String doPostRequest(String requestUrl, Map<String, String> requestContent,
                                       Map<String, String> requestHeaders) {
        return doPostRequest(requestUrl, requestContent, requestHeaders, null);
    }

    public static String doPostRequest(String requestUrl, Map<String, String> requestContent,
                                       Map<String, String> requestHeaders, NetWorkTimeout timeout) {
        return doRequest(requestUrl, NetWorkConstant.HTTP_METHOD_POST, requestContent, requestHeaders, timeout);
    }

    public static String doRequest(String requestUrl, String httpMethod, String requestContent) {
        if (StringUtils.isBlank(requestUrl) || StringUtils.isBlank(httpMethod)) {
            log.error(">>> doRequest params can't be null: request:[{}], httpMethod:[{}]",
                    requestUrl, httpMethod);
            return null;
        }
        return NetWorkExecutor.executeUrlConnection(requestUrl, requestContent, httpMethod, null, null, null);
    }

    private static String doRequest(String requestUrl, String httpMethod, Map<String, String> requestContent,
                                    Map<String, String> requestHeaders, NetWorkTimeout timeout) {
        if (StringUtils.isBlank(requestUrl) || StringUtils.isBlank(httpMethod)) {
            log.error(">>> doRequest params can't be null: request:[{}], httpMethod:[{}]",
                    requestUrl, httpMethod);
            return null;
        }
        StringBuilder reqContent = new StringBuilder();
        try {
            if (requestContent != null && !requestContent.isEmpty()) {
                for (Entry<String, String> elem : requestContent.entrySet()) {
                    reqContent.append(elem.getKey())
                            .append("=")
                            .append(URLEncoder.encode(elem.getValue(), "UTF-8"))
                            .append("&");
                }
                //去最后一个&
                reqContent.deleteCharAt(reqContent.length() - 1);
            }
        } catch (Exception e) {
            log.error(">>> doRequest encode request content error");
            return null;
        }

        return NetWorkExecutor.executeUrlConnection(requestUrl, reqContent.toString(),
                httpMethod, requestHeaders, timeout, null);
    }

}
