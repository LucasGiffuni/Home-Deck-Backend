package com.home.app.service.Spotify;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class TokenAuthorization {
    private final String clientId = "d61046f675cf4c26b2fa9b24b8a839ca";
    private final String clientSecret = "c87cfb81a09941cdb8fd13c0cd1b0157";
    private final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/public/hello");

    private final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();
            
    private final AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
            .state("")
            .scope("user-read-email,user-modify-playback-state")
            .show_dialog(true)
            .build();

    public String authorizationCodeUri_Sync() {

        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

}
