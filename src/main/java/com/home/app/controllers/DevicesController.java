package com.home.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.app.service.impl.DeviceServiceImpl;
import com.home.app.AppApplication;

@RestController
public class DevicesController {

    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/devices/lights/getLightDevices")
    public ResponseEntity<?> getLightDevices() {
        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.returnLights());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/public/lights/modifyLightDevice")
    public ResponseEntity<?> modifyLightDevice(
            @RequestParam String Field,
            @RequestParam String Value,
            @RequestParam Integer DeviceID) {

        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.modifyLightValues(DeviceID, Field, Value));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/devices/lights/getOpenDevices")
    public ResponseEntity<?> getOpenDevices() {
        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.returnOpens());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/devices/lights/getOpensFromRoom")
    public ResponseEntity<?> getOpensFromRoom(@RequestParam String RoomID) {
        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.returnOpen(RoomID));
    }

}
