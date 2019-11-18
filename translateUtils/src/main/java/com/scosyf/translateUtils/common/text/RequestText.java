package com.scosyf.translateUtils.common.text;

import com.scosyf.translateUtils.common.AbstractRequest;
import com.scosyf.translateUtils.helper.ToolKit;
import com.scosyf.translateUtils.helper.YoudaoConfig;
import com.scosyf.translateUtils.helper.YoudaoConstant;

public class RequestText extends AbstractRequest {

    /**
     * create by kunbu
     */
    private static final long serialVersionUID = 8375657718362317818L;

    private String q;

    public static RequestText of(String query) throws Exception {
        return of(query, YoudaoConstant.CH, YoudaoConstant.EN);
    }

    public static RequestText of(String query, String from, String to) throws Exception {
        RequestText params = new RequestText();
        //这里要对查询的内容进行URLencode，但是签名的时候不需要
        params.setQ(ToolKit.encode(query));
        params.setAppKey(YoudaoConfig.appKey);
        params.setFrom(from);
        params.setTo(to);
        params.setSalt(YoudaoConfig.SALT);
        params.setSign(ToolKit.md5Encode(YoudaoConfig.appKey + query + YoudaoConfig.SALT + YoudaoConfig.appSecret));
        return params;
    }

    public static String ofUrl(String query) throws Exception {
        RequestText params = of(query);
        return ofUrl(params);
    }

    public static String ofUrl(String query, String from, String to) throws Exception {
        RequestText params = of(query, from, to);
        return ofUrl(params);
    }

    public static String ofUrl(RequestText params) {
        StringBuilder urlBuilder = new StringBuilder(YoudaoConfig.BASE_HTTP_PATH + YoudaoConfig.TEXT_API);
        urlBuilder.append("?")
                .append("q").append("=").append(params.getQ()).append("&")
                .append("from").append("=").append(params.getFrom()).append("&")
                .append("to").append("=").append(params.getTo()).append("&")
                .append("appKey").append("=").append(params.getAppKey()).append("&")
                .append("salt").append("=").append(params.getSalt()).append("&")
                .append("sign").append("=").append(params.getSign());
        return urlBuilder.toString();
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    @Override
    public String toString() {
        return "RequestParamText [q=" + q + "]" + super.toString();
    }

}
