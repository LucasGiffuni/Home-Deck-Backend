package com.home.app.service.Spotify;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import org.apache.hc.core5.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.home.app.AppApplication;

import io.jsonwebtoken.ExpiredJwtException;

import java.io.IOException;
import java.net.URI;

public class RefreshTokenAuthorization {

    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    private String clientId = "";
    private String clientSecret = "";
    private URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/public/hello");
    private String refreshToken;

    private String code;
    private SpotifyApi spotifyApi;
    private AuthorizationCodeRequest authorizationCodeRequest;
    private AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest;
    private SpotifyApi spotifyApiRefresh;

    public RefreshTokenAuthorization(String clientID, String clientSecret, String code) {
        this.code = code;
        this.clientId = clientID;
        this.clientSecret = clientSecret;

        spotifyApi = new SpotifyApi.Builder().setClientId(clientId).setClientSecret(clientSecret)
                .setRedirectUri(redirectUri).build();
        authorizationCodeRequest = spotifyApi.authorizationCode(code).build();

    }

    public RefreshTokenAuthorization(String clientID, String clientSecret, String refresh, String code) {
        logger.info("Refresh: " + refresh);
        this.refreshToken = refresh;
        this.code = code;
        this.clientId = clientID;
        this.clientSecret = clientSecret;

        spotifyApiRefresh = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRefreshToken(refresh)
                .build();
        authorizationCodeRefreshRequest = spotifyApiRefresh.authorizationCodeRefresh()
                .build();

    }

    public String[] authorizationCode_Sync() {
        try {
            AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
            String[] response = new String[2];
            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            response[0] = authorizationCodeCredentials.getAccessToken();
            response[1] = authorizationCodeCredentials.getRefreshToken();

            this.refreshToken = authorizationCodeCredentials.getRefreshToken();

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
            return response;
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ExpiredJwtException expired) {
            logger.info("Security exception: " + expired.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token Expired");
        }
        return null;
    }

    public void authorizationCodeRefresh_Sync() {
        try {
            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApiRefresh.setAccessToken(authorizationCodeCredentials.getAccessToken());

            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ExpiredJwtException expired) {
            logger.info("Security exception: " + expired.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token Expired");
        }
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
