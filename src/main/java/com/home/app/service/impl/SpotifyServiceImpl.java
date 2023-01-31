package com.home.app.service.impl;

import java.io.IOException;
import java.net.URI;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;


import com.home.app.AppApplication;
import com.home.app.service.Spotify.RefreshTokenAuthorization;
import com.home.app.service.Spotify.TokenAuthorization;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;


import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class SpotifyServiceImpl {

    @Autowired
    RestTemplate restTemplate = new RestTemplate();

    private String clientId;
    private String clientSecret;
    private URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/public/hello");

    @Value("${jwtSecurity.JWT_SECRET_KEY}")
    private String refreshToken;

    private String code = "";
    private String accessToken = "";
    private String state = "";
    // ############## Spotify things ##############
    private SpotifyApi spotifyApi;

    private ClientCredentialsRequest clientCredentialsRequest;

    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    private AuthorizationCodeRequest authorizationCodeRequest;

    private final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    public SpotifyServiceImpl(String clientID, String clientSecret) {
        this.clientId = clientID;
        this.clientSecret = clientSecret;

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientID)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .setRefreshToken(refreshToken)
                .build();
        clientCredentialsRequest = spotifyApi.clientCredentials().build();

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state(state)
                .scope("user-modify-playback-state,user-read-email")
                .show_dialog(true)
                .build();

        authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();

        logger.info("ID: " + clientID);
        logger.info("Secret: " + clientSecret);
    }

    // ####### SPOTIFY TOKEN MANAGER #######
    public String ObtainToken() {
        String token = "null";
        try {
            final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest
                    .executeAsync();

            final ClientCredentials clientCredentials = clientCredentialsFuture.join();
            clientCredentials.builder().setAccessToken(clientCredentials.getAccessToken());
            token = clientCredentials.getAccessToken();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
        return token;

    }

    public ResponseEntity<?> ObtainTokenWithCode() {
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
            return new ResponseEntity<String>(authorizationCodeCredentials.toString(), null, HttpStatus.CREATED);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public String[] ObtainRefreshToken(String code) {

        RefreshTokenAuthorization rftA = new RefreshTokenAuthorization(clientId, clientSecret, code);

        String[] response = rftA.authorizationCode_Sync();

        return response;

    }

    public ResponseEntity<?> RefreshToken(String refreshToken, String code) {

        RefreshTokenAuthorization rftA = new RefreshTokenAuthorization(clientId, clientSecret, refreshToken, code);

        rftA.authorizationCodeRefresh_Sync();

        return new ResponseEntity<String>("OK", null, HttpStatus.OK);

    }

    public ResponseEntity<?> ObtainCode() {
        TokenAuthorization tA = new TokenAuthorization(this.clientId, this.clientSecret);

        tA.authorizationCodeUri_Sync();

        return new ResponseEntity<String>(tA.authorizationCodeUri_Sync(), HttpStatus.OK);

    }

    public String obtainURL() {

        final URI uri = authorizationCodeUriRequest.execute();

        return uri.toString();

    }

    public String normalizeURL(String url) {

        int pos = url.indexOf("://");
        String newLink = "http" + url.substring(pos);

        return newLink;
    }

    // GETTERS SETTERS

    public void setCode(String code) {
        this.code = code;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getCode() {
        return code;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
