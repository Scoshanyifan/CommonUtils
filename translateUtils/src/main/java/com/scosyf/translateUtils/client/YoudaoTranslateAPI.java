package com.scosyf.translateUtils.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scosyf.translateUtils.common.ResponseHelper;
import com.scosyf.translateUtils.common.text.RequestText;
import com.scosyf.translateUtils.common.text.ResultDataText;
import com.scosyf.translateUtils.helper.YoudaoConstant;
import com.scosyf.translateUtils.http.YoudaoHttpClient;


public class YoudaoTranslateAPI {
    
    private static Logger log = LoggerFactory.getLogger(YoudaoHttpClient.class);
    
    /**
     * 英翻中
     * @param word
     * @return 翻译结果，若查询异常返回原始数据
     */
    public static String getTranslateEN2CH(String english) {
        ResultDataText res = getTranslate(english, YoudaoConstant.EN, YoudaoConstant.CH);
        if (res != null) {
            return res.getTranslate();
        } else {
            return english;
        }
    }
    
    /**
     * 中翻英
     * @param chinese
     * @return 翻译结果，若查询异常返回原始数据
     */
    public static String getTranslateCH2EN(String chinese) {
        ResultDataText res = getTranslate(chinese, YoudaoConstant.CH, YoudaoConstant.EN);
        if (res != null) {
            return res.getTranslate();
        } else {
            return chinese;
        }
    }
    
    /**
     * 中翻日
     * @param chinese
     * @return 翻译结果，若查询异常返回原始数据
     */
    public static String getTranslateCH2JP(String chinese) {
        ResultDataText res = getTranslate(chinese, YoudaoConstant.CH, YoudaoConstant.JA);
        if (res != null) {
            return res.getTranslate();
        } else {
            return chinese;
        }
    }
    
    /**
     * 翻译
     * @param query 查询内容
     * @param from 原始语言
     * @param to 目标语言
     * @return 翻译失败返回null
     */
    public static ResultDataText getTranslate(String query, String from, String to) {
        log.info(">>> [getTranslate] query:[{}], from:[{}], to:[{}]", new Object[]{query, from, to});
        String requestUrl = null;
        try {
            if (from != null && to != null) {
                requestUrl = RequestText.ofUrl(query, from, to);
            } else {
                //默认中翻英
                requestUrl = RequestText.ofUrl(query);
            }
        } catch (Exception e) {
            log.error(">>> get requestUrl failure, e:[{}]", e);
            return null;
        }
        return getTranslateResult(requestUrl);
    }
    
    private static ResultDataText getTranslateResult(String requestUrl) {
        String responseStr = YoudaoHttpClient.getTranslateRes(requestUrl);
        ResultDataText translateRes = ResponseHelper.getTanslateResult(responseStr);
        if (translateRes != null) {
            return translateRes;
        } else {
            return null;
        }
    }
    
    public static void main(String[] args) {
        System.out.println(getTranslateEN2CH("holy shit"));
        System.out.println(getTranslateCH2EN("舰长补给全保底，舰长副本零掉落"));
        System.out.println(getTranslateCH2JP("今晚月色真美"));
//        System.out.println(getTranslate("~!&（……&……%&&'", "HJ", "sss"));
    }
}
