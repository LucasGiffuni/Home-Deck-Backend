package com.home.app.service.impl;

import com.home.app.model.response.DefaultResponse;
import com.home.app.model.response.DefaultResponseData;
import com.home.app.service.HomeService;

public class HomeServiceImpl implements HomeService {

    public DefaultResponse ObtenerResumen() {
        DefaultResponse df = new DefaultResponse();
        df.setDefaultResponse("200", "ok");

        return df;
    }

}
