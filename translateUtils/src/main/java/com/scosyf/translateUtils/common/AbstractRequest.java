package com.scosyf.translateUtils.common;

import java.io.Serializable;

public abstract class AbstractRequest implements Serializable{

    /**
     * create by kunbu
     */
    private static final long serialVersionUID = -303425764359342657L;
    
    public String from;
    public String to;
    public String appKey;
    public String salt;
    public String sign;
    
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getAppKey() {
        return appKey;
    }
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getSign() {
        return sign;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }
    @Override
    public String toString() {
        return "RequestParamBase [from=" + from + ", to=" + to + ", appKey=" + appKey + ", salt=" + salt + ", sign="
                + sign + "]";
    }
    
}
