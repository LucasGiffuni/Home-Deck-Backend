package com.home.app.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.token.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.gax.rpc.ApiException;
import com.home.app.AppApplication;
import com.home.app.integrations.DialogFlow;
import com.home.app.model.AuthenticationReq;
import com.home.app.model.TokenInfo;
import com.home.app.model.response.DefaultResponse;
import com.home.app.model.response.DialogFlowIntentResponse;
import com.home.app.security.JwtUtilService;
import com.home.app.service.impl.*;

@RestController
@CrossOrigin
public class HomeController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService usuarioDetailsService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    static DialogFlowIntentResponse response;


    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @RequestMapping("/resume")
    public DefaultResponse nose() {
        HomeServiceImpl hsi = new HomeServiceImpl();

        return hsi.ObtenerResumen();
    }

    // Google DialogFlow integration - with a word detect a intent.
    @RequestMapping("/home")
    public static ResponseEntity<?> Dialog(@RequestParam String textToIdentify)
            throws ApiException, IOException {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        logger.info("Datos del Usuario: {}", auth.getPrincipal());
        logger.info("Datos de los Permisos {}", auth.getAuthorities());
        logger.info("Esta autenticado {}", auth.isAuthenticated());

        

        response = DialogFlow.detectIntentSentimentAnalysis(textToIdentify,"home-deck-sumy", "106029489887999475024", "es_UY");
        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("Response", "Detected Intent: " + response.getDiaglogFlowResponse().getDetectedIntent() +
                " \n " + "Score: " + response.getDiaglogFlowResponse().getSentimentScore() +
                " \n " + "FulFillment Text: " + response.getDiaglogFlowResponse().getFulFillmentText());

        return ResponseEntity.ok(mensaje);
    }

    // ###################### TOKEN MANAGER ######################

    @GetMapping("/admin")
    public ResponseEntity<?> getMensajeAdmin() {

        var auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("Datos del Usuario: {}", auth.getPrincipal());
        logger.info("Datos de los Permisos {}", auth.getAuthorities());
        logger.info("Esta autenticado {}", auth.isAuthenticated());

        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("contenido", "Hola Admin");
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/public/authenticate")
    public ResponseEntity<TokenInfo> authenticate(@RequestBody AuthenticationReq authenticationReq) {
        logger.info("Autenticando al usuario {}", authenticationReq.getUsuario());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationReq.getUsuario(),
                        authenticationReq.getClave()));

        final UserDetails userDetails = usuarioDetailsService.loadUserByUsername(
                authenticationReq.getUsuario());

        final String jwt = jwtUtilService.generateToken(userDetails);

        TokenInfo tokenInfo = new TokenInfo(jwt);

        return ResponseEntity.ok(tokenInfo);
    }

}
