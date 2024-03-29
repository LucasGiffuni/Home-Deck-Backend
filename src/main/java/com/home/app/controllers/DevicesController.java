package com.home.app.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.app.service.impl.DeviceServiceImpl;
import com.home.app.AppApplication;

@CrossOrigin(origins = "*")
@RestController
public class DevicesController {

    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    @GetMapping("/devices/lights/getLightDevices")
    public ResponseEntity<?> getLightDevices() {
        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.returnLights());
    }

    @GetMapping("/devices/lights/getLightsFromRoom")
    public ResponseEntity<?> getLightsFromRoom(@RequestParam String RoomID) {
        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.returnLightsFromRoom(RoomID));
    }

    @PostMapping("/public/lights/modifyLightDevice")
    public ResponseEntity<?> modifyLightDevice(
            @RequestParam String Field,
            @RequestParam String Value,
            @RequestParam Integer DeviceID) {

        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.modifyLightValues(DeviceID, Field, Value));
    }

    @PostMapping("/public/lights/createLightDevice")
    public ResponseEntity<?> createLightDevice(
            @RequestParam Integer DeviceID,
            @RequestParam String RoomID,
            @RequestParam String Name,
            @RequestParam String Type,
            @RequestParam Boolean State) {

        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.createLightDevice(DeviceID, RoomID, Name, Type, State));
    }

    @GetMapping("/devices/lights/getOpenDevices")
    public ResponseEntity<?> getOpenDevices() {
        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.returnOpens());
    }

    @GetMapping("/devices/lights/getOpensFromRoom")
    public ResponseEntity<?> getOpensFromRoom(@RequestParam String RoomID) {
        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.returnOpen(RoomID));
    }

    @PostMapping("/public/lights/modifyOpenDevice")
    public ResponseEntity<?> modifyOpenDevice(
            @RequestParam String Field,
            @RequestParam String Value,
            @RequestParam Integer DeviceID) {

        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.modifyOpenDevice(DeviceID, Field, Value));
    }

    @GetMapping("/devices/layouts/getRoomLayout")
    public ResponseEntity<?> getRoomLayout(@RequestParam String RoomID) {
        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.returnRoomLayout(RoomID));
    }

    @PostMapping("/public/lights/setRoomLayout")
    public ResponseEntity<?> setRoomLayout(
            @RequestParam String RoomID,
            @RequestBody String Layout) {

        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.setRoomLayout(RoomID, Layout));
    }

    @PostMapping("/public/lights/setRoomLayoutState")
    public ResponseEntity<?> setRoomLayoutState(
            @RequestParam String RoomID,
            @RequestParam String state) {

        DeviceServiceImpl deviceService = new DeviceServiceImpl();

        return ResponseEntity.ok(deviceService.setRoomLayoutState(RoomID, state));
    }

}
