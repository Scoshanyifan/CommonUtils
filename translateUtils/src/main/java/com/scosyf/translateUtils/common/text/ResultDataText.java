package com.scosyf.translateUtils.common.text;

import com.scosyf.translateUtils.common.ResultData;

public class ResultDataText extends ResultData{

    /**
     * create by kunbu
     */
    private static final long serialVersionUID = -3980582312609967024L;
    
    private String origin;
    private String translate;
    
    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public String getTranslate() {
        return translate;
    }
    public void setTranslate(String translate) {
        this.translate = translate;
    }
    @Override
    public String toString() {
        return "ResultDataText [origin=" + origin + ", translate=" + translate + "]";
    }
    
}
