package com.scosyf.translateUtils.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.scosyf.translateUtils.common.text.ResponseText;
import com.scosyf.translateUtils.common.text.ResultDataText;

public class ResponseHelper {
    
    private static Logger log = LoggerFactory.getLogger(ResponseHelper.class);
    
    /**
     * 获取翻译结果
     * @param requestUrl
     * @return
     * create by kunbu
     */
    public static ResultDataText getTanslateResult(String responseStr) {
        if (responseStr != null) {
            ResponseText response = handlerResponse(responseStr);
            if (response != null) {
                log.info(">>> get translate result success, response:[{}]", response);
                return convertResponse(response);
            } else {
                log.error(">>> get translate result error, return:[{}]", responseStr);
                return null;
            }
        } else {
            log.error(">>> get translate result failure, return null");
            return null;
        }
    }
    
    private static ResultDataText convertResponse(ResponseText response) {
        ResultDataText transRes = new ResultDataText();
        transRes.setOrigin(response.getQuery());
        StringBuilder basic = new StringBuilder();
        for (String trans : response.getTranslation()) {
            basic.append(trans).append(",");
        }
        basic.deleteCharAt(basic.length() - 1);
        transRes.setTranslate(basic.toString());
        return transRes;
    }
    
    private static ResponseText handlerResponse(String jsonStr) {
        JSONObject json = JSONObject.parseObject(jsonStr);
        if (json.getString("errorCode").equals("0")) {
            //返回成功
            log.info(">>> translate success <<<");
            return convertJson(json);
        } else {
            //失败返回null
            log.error(">>> translate failure <<<");
            return null;
        }
    }

    private static ResponseText convertJson(JSONObject json) {
        ResponseText response = new ResponseText();
        response.setQuery(json.getString("query"));
        response.setL(json.getString("l"));
        response.settSpeakUrl(json.getString("tSpeakUrl"));
        response.setSpeakUrl(json.getString("speakUrl"));
        JSONArray transArr = json.getJSONArray("translation");
        if (!transArr.isEmpty()) {
            List<String> translation = Lists.newArrayList();
            for (Object elem : transArr) {
                translation.add((String) elem);
            }
            response.setTranslation(translation);
        }
        if (json.get("basic") != null) {
            JSONObject basic = json.getJSONObject("basic");
            response.setUk_phonetic(basic.getString("uk-phonetic"));
            response.setUs_phonetic(basic.getString("us-phonetic"));
            JSONArray expArr = basic.getJSONArray("explains");
            if (!expArr.isEmpty()) {
                List<String> explains = Lists.newArrayList();
                for (Object elem : expArr) {
                    explains.add((String) elem);
                }
                response.setExplains(explains);
            }
        }
        if (json.get("web") != null) {
            
        }
        if (json.get("dict") != null) {
            
        }
        if (json.get("webdict") != null) {
            
        }
        if (json.get("tSpeakUrl") != null) {
            response.settSpeakUrl(json.getString("tSpeakUrl"));
        }
        if (json.get("speakUrl") != null) {
            response.setSpeakUrl(json.getString("speakUrl"));
        }
        return response;
    }
}
