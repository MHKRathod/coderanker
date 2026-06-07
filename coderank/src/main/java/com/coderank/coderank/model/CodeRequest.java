package com.coderank.coderank.model;

public class CodeRequest {
    private String code;
    private String language;

    public String getCode(){
        return code;
    }

    public String getLanguage() {
        return language;
    }

    public void setCode(String code){
        this.code = code;
    }
    public void setLanguage(String language){
        this.language = language;
    }
}
