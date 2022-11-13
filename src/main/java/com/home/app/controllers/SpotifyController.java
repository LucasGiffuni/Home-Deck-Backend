package com.home.app.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.home.app.AppApplication;

import com.home.app.service.Spotify.SpotifyActions;
import com.home.app.service.impl.SpotifyServiceImpl;

@RestController
@CrossOrigin
public class SpotifyController {

    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    private String CODE;
    private String STATE;
    private String REFRESH_TOKEN;
    private String ACCESS_TOKEN;

    private SpotifyServiceImpl sp;

    @GetMapping("/spotify/getMe")
    public ResponseEntity<?> getMe() {

        SpotifyActions spotifyActions = new SpotifyActions(ACCESS_TOKEN, CODE, STATE);

        return spotifyActions.ObtainMe();
    }

    @PostMapping(value = "/spotify/pausePlayer")
    public ResponseEntity<?> pausePlayer() {

        SpotifyActions spotifyActions = new SpotifyActions(ACCESS_TOKEN, CODE, STATE);

        return spotifyActions.pausePlayer();
    }
    @PostMapping(value = "/spotify/resumePlayer")
    public ResponseEntity<?> resumePlayer() {

        SpotifyActions spotifyActions = new SpotifyActions(ACCESS_TOKEN, CODE, STATE);

        return spotifyActions.resumePlayer();
    }

    @GetMapping(value = "/spotify/getPlayerInformation")
    public ResponseEntity<?> getPlayerInformation() {

        SpotifyActions spotifyActions = new SpotifyActions(ACCESS_TOKEN, CODE, STATE);

        return spotifyActions.getPlayerInformation();
    }

    @PostMapping(value = "/spotify/setPlayerVolume")
    public ResponseEntity<?> setPlayerVolume(@RequestParam int volume) {

        SpotifyActions spotifyActions = new SpotifyActions(ACCESS_TOKEN, CODE, STATE);

        return spotifyActions.setPlayerVolume(volume);
    }


    @GetMapping(value = "/spotify/getUserPlaylists")
    public ResponseEntity<?> getUserAlbums() {

        SpotifyActions spotifyActions = new SpotifyActions(ACCESS_TOKEN, CODE, STATE);

        return spotifyActions.getUserPlaylists();
    }

    // ##### SPOTIFY TOKEN MANAGER #####
    @RequestMapping("/public/hello")
    public ResponseEntity<?> Hello(@RequestParam String code, @RequestParam String state) {

        this.STATE = state;
        this.CODE = code;

        sp.setCode(code);
        sp.setAccessToken(code);
        sp.setState(state);

        return new ResponseEntity<String>("Code: " + sp.getCode() + " State: " + this.STATE, null, HttpStatus.OK);
    }

    @GetMapping("/spotify/getToken")
    public ResponseEntity<?> getToken() {

        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("contenido", sp.ObtainToken());
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/spotify/getCodeFromURL")
    public ResponseEntity<?> getCodeFromURL(@RequestParam String clientID, @RequestParam String clientSecret) {

        SpotifyServiceImpl local = new SpotifyServiceImpl(clientID, clientSecret);
        this.setSp(local);

        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("contenido", this.getSp().ObtainCode().toString());
        return ResponseEntity.ok(mensaje);
    }

    @GetMapping("/spotify/getRefreshToken")
    public ResponseEntity<?> getRefreshToken() {
        String[] response = this.getSp().ObtainRefreshToken(CODE);

        ACCESS_TOKEN = response[0];
        REFRESH_TOKEN = response[1];

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/spotify/refreshToken")
    public ResponseEntity<?> refreshToken() {

        return this.getSp().RefreshToken(REFRESH_TOKEN, CODE);
    }

    @GetMapping("/spotify/getTokenWithCode")
    public ResponseEntity<?> getTokenWithCode() {

        Map<String, String> mensaje = new HashMap<>();
        mensaje.put("contenido", sp.ObtainTokenWithCode().toString());
        return ResponseEntity.ok(mensaje);
    }

    public SpotifyServiceImpl getSp() {
        return sp;
    }

    public void setSp(SpotifyServiceImpl sp) {
        this.sp = sp;
    }

}
