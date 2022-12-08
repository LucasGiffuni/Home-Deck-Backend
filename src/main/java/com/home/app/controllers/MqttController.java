package com.home.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.app.service.impl.DeviceServiceImpl;
import com.google.api.client.json.Json;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.home.app.AppApplication;
import com.home.app.configurations.MqttGateway;

@RestController
public class MqttController {

    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);
    /*
     * @Autowired
     * MqttGateway gateway;
     * 
     * @CrossOrigin(origins = "http://localhost:3000")
     * 
     * @PostMapping("/public/lights/nose")
     * public ResponseEntity<?> modifyLightDevice(
     * 
     * @RequestParam String message,
     * 
     * @RequestParam String topic) {
     * try {
     * 
     * gateway.senToMqtt(message, topic);
     * return ResponseEntity.ok("Success");
     * } catch (Exception ex) {
     * ex.printStackTrace();
     * return ResponseEntity.ok("fail");
     * }
     * }
     */
}
