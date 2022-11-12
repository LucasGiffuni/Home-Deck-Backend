package com.home.app.service.Spotify;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.home.app.AppApplication;
import com.home.app.service.Spotify.RefreshTokenAuthorization;
import com.home.app.service.Spotify.TokenAuthorization;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.pkce.AuthorizationCodePKCERequest;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.player.PauseUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetUsersProfileRequest;
import se.michaelthelin.spotify.model_objects.specification.User;

import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class SpotifyActions {

    private final String userId = "12179854506";


    private String code = "";
    private String accessToken = "";
    private String state = "";

    private final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    private SpotifyApi spotifyApi;
    private GetUsersProfileRequest getUsersProfileRequest;
    private PauseUsersPlaybackRequest pauseUsersPlaybackRequest;

    public SpotifyActions(String refreshToken, String code, String state) {
        this.code = code;
        this.accessToken = refreshToken;
        this.state = state;

        logger.info("RefreshToken: " + this.accessToken);

        spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(accessToken)
                .build();

        getUsersProfileRequest = spotifyApi.getUsersProfile(userId)
                .build();

        pauseUsersPlaybackRequest = spotifyApi.pauseUsersPlayback()
                // .device_id("5fbb3ba6aa454b5534c4ba43a8c7e8e45a63ad0e")
                .build();

    }

    public ResponseEntity<?> ObtainMe() {

        try {
            final User user = getUsersProfileRequest.execute();


            return new ResponseEntity<String>("Display name: " + user.getDisplayName(), null, HttpStatus.OK);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }

        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<?> pausePlayer() {
        try {
            final String string = pauseUsersPlaybackRequest.execute();

            return new ResponseEntity<String>("Null: " + string, null, HttpStatus.OK);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    
}
