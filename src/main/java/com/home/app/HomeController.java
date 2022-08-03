package com.home.app;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.gax.rpc.ApiException;
import com.google.cloud.dialogflow.v2.QueryResult;
import com.home.app.integrations.DialogFlow;
import com.home.app.model.response.DefaultResponse;
import com.home.app.model.response.DefaultResponseData;
import com.home.app.model.response.DialogFlowIntentResponse;
import com.home.app.model.response.DialogFlowIntentResponseData;
import com.home.app.service.impl.*;

@RestController
public class HomeController {


    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }
    
    @PostMapping("/hello2")
    public String hello2(){
        DialogFlowImpl impl = new DialogFlowImpl();

        //impl.service(request, response);
        return "hello";
    }

    @RequestMapping("/resume")
    public DefaultResponse nose(){
        HomeServiceImpl hsi = new HomeServiceImpl();

        return hsi.ObtenerResumen();
    }

    @PostMapping("/CreateIntent")
    public String CreateIntent() throws ApiException, IOException{
       DialogFlow df = new DialogFlow();
       List<String> frase1 = new ArrayList<String>();
       frase1.add("prueba");
       List<String> frase2 = new ArrayList<String>();
       frase2.add("prueba");

       df.createIntent("prueba", "home-deck-sumy", frase1, frase2);
        return "ok";
    }


    @RequestMapping("/home")
    public static DialogFlowIntentResponse Dialog(@RequestParam String textToIdentify) throws ApiException, IOException{

        

        return DialogFlow.detectIntentSentimentAnalysis("home-deck-sumy", textToIdentify, "106029489887999475024", "es_UY");
    }



}
