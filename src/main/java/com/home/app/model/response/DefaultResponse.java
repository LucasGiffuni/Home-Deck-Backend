package com.home.app.model.response;

public class DefaultResponse {

    DefaultResponseData DefaultResponse;

    public DefaultResponseData getDefaultResponse() {
        return DefaultResponse;
    }

    public void setDefaultResponse(String code, String message) {
        DefaultResponseData df = new DefaultResponseData();
        df.setCode(code);
        df.setMessage(message);
        this.DefaultResponse = df;
    }

}
