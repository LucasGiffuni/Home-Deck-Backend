package com.home.app.model;

import java.util.Map;

public class Opens {
    
    @JsonProperty("opens")
    private Open[] opens;

    public Open[] getOpens() {
        return opens;
    }

    public void setOpens(Open[] value) {
        this.opens = value;
    }
}
