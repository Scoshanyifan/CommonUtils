package com.scosyf.translateUtils.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scosyf.utils.net.client.HttpConnectionClient;


public class YoudaoHttpClient {

    private static Logger log = LoggerFactory.getLogger(YoudaoHttpClient.class);

    public static String getTranslateRes(String requestUrl) {
        log.info(">>> http begin <<< requestUrl: [{}]", requestUrl);
        try {
            String jsonStr = HttpConnectionClient.doGetRequest(requestUrl);
            log.info(">>>  http end  <<< responseStr: [{}]", jsonStr);
            return jsonStr;
        } catch (Exception e) {
            log.error(">>> http failure <<< error:[{}]" + e);
            return null;
        }
    }

}
