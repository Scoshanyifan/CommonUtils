package com.scosyf.translateUtils.common;

import java.io.Serializable;

public abstract class AbstractResponse implements Serializable {

    /**
     * create by kunbu
     */
    private static final long serialVersionUID = 2500695833563890106L;
  
    public String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return "ResponseParamBase [errorCode=" + errorCode + "]";
    }
    
}
