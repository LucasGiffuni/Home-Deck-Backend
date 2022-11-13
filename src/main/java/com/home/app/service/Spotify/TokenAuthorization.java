package com.home.app.service.Spotify;

import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.net.URI;

public class TokenAuthorization {
    private String clientId;
    private String clientSecret;
    private final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/public/hello");

    private SpotifyApi spotifyApi;

    private AuthorizationCodeUriRequest authorizationCodeUriRequest;

    private final String scopes = "ugc-image-upload,user-read-playback-state,app-remote-control,user-modify-playback-state,playlist-read-private,user-follow-modify,playlist-read-collaborative,user-follow-read,user-read-currently-playing,user-read-playback-position,user-library-modify,playlist-modify-private,playlist-modify-public,user-read-email,user-top-read,streaming,user-read-recently-played,user-read-private,user-library-read";

    public TokenAuthorization(String clientID, String clientSecret) {
        this.clientId = clientID;
        this.clientSecret = clientSecret;

        spotifyApi = new SpotifyApi.Builder()
                .setClientId(this.clientId)
                .setClientSecret(this.clientSecret)
                .setRedirectUri(this.redirectUri)
                .build();

        authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("")
                .scope(scopes)
                .show_dialog(true)
                .build();
    }

    public String authorizationCodeUri_Sync() {

        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

}
