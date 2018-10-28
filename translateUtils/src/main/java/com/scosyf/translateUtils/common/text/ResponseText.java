package com.scosyf.translateUtils.common.text;

import java.util.List;

import com.scosyf.translateUtils.common.AbstractResponse;


public class ResponseText extends AbstractResponse{

    /**
     * create by kunbu
     */
    private static final long serialVersionUID = -3300414313931422492L;
    
    private String          query;
    private List<String>    translation;
    // 有道词典-基本词典,查词时才有
    private String          uk_phonetic;
    private String          us_phonetic;
    private List<String>    explains;
    // 有道词典-网络释义，该结果不一定存在
    private String          web;
    private String          l;
    private String          dict;
    private String          webdict;
    private String          tSpeakUrl;
    private String          speakUrl;
    
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public String getUk_phonetic() {
        return uk_phonetic;
    }

    public void setUk_phonetic(String uk_phonetic) {
        this.uk_phonetic = uk_phonetic;
    }

    public String getUs_phonetic() {
        return us_phonetic;
    }

    public void setUs_phonetic(String us_phonetic) {
        this.us_phonetic = us_phonetic;
    }

    public List<String> getExplains() {
        return explains;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getDict() {
        return dict;
    }

    public void setDict(String dict) {
        this.dict = dict;
    }

    public String getWebdict() {
        return webdict;
    }

    public void setWebdict(String webdict) {
        this.webdict = webdict;
    }

    public String gettSpeakUrl() {
        return tSpeakUrl;
    }

    public void settSpeakUrl(String tSpeakUrl) {
        this.tSpeakUrl = tSpeakUrl;
    }

    public String getSpeakUrl() {
        return speakUrl;
    }

    public void setSpeakUrl(String speakUrl) {
        this.speakUrl = speakUrl;
    }

    @Override
    public String toString() {
        return "ResponseParamText [query=" + query + ", translation=" + translation + ", uk_phonetic=" + uk_phonetic
                + ", us_phonetic=" + us_phonetic + ", explains=" + explains + ", web=" + web + ", l=" + l + ", dict="
                + dict + ", webdict=" + webdict + ", tSpeakUrl=" + tSpeakUrl + ", speakUrl=" + speakUrl + "]";
    }

}
