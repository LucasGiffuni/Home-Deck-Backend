package com.home.app.service.Spotify;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import ch.qos.logback.core.subst.Token;

public class TokenAuthorization {
    private String clientId;
    private String clientSecret;
    private final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/public/hello");

    private SpotifyApi spotifyApi;

    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    public TokenAuthorization(String clientID, String clientSecret) {
        this.clientId = clientID;
        this.clientSecret = clientSecret;

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("")
                .scope("user-read-email,user-modify-playback-state")
                .show_dialog(true)
                .build();
    }

    public String authorizationCodeUri_Sync() {

        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

}
