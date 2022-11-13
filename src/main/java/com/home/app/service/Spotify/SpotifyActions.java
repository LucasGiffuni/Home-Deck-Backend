package com.home.app.service.Spotify;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.home.app.AppApplication;

import io.jsonwebtoken.ExpiredJwtException;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.requests.data.library.GetCurrentUsersSavedAlbumsRequest;
import se.michaelthelin.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.PauseUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.SetVolumeForUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetUsersProfileRequest;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistSimplified;
import se.michaelthelin.spotify.model_objects.specification.SavedAlbum;
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
    private StartResumeUsersPlaybackRequest startResumeUsersPlaybackRequest;
    private GetInformationAboutUsersCurrentPlaybackRequest getInformationAboutUsersCurrentPlaybackRequest;
    private SetVolumeForUsersPlaybackRequest setVolumeForUsersPlaybackRequest;
    private GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest;

    public SpotifyActions(String refreshToken, String code, String state) {
        this.code = code;
        this.accessToken = refreshToken;
        this.state = state;

        logger.info("RefreshToken: " + this.accessToken);

        spotifyApi = new SpotifyApi.Builder()
                .setAccessToken(this.accessToken)
                .build();

        getUsersProfileRequest = spotifyApi.getUsersProfile(this.userId)
                .build();

        pauseUsersPlaybackRequest = spotifyApi.pauseUsersPlayback()
                .build();
        startResumeUsersPlaybackRequest = spotifyApi
                .startResumeUsersPlayback()
                .build();
        getInformationAboutUsersCurrentPlaybackRequest = spotifyApi.getInformationAboutUsersCurrentPlayback()
                .build();

        getListOfCurrentUsersPlaylistsRequest = spotifyApi
                .getListOfCurrentUsersPlaylists()
                // .limit(10)
                // .offset(0)
                .build();

    }

    public ResponseEntity<?> ObtainMe() {

        try {
            final User user = getUsersProfileRequest.execute();

            return new ResponseEntity<String>("Display name: " + user.getDisplayName(), null, HttpStatus.OK);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ExpiredJwtException expired) {
            logger.info("Security exception: " + expired.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token Expired");
        }

        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    public ResponseEntity<?> pausePlayer() {
        try {
            pauseUsersPlaybackRequest.execute();

            return new ResponseEntity<String>("Player paused.", null, HttpStatus.OK);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ExpiredJwtException expired) {
            logger.info("Security exception: " + expired.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token Expired");
        }

        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> resumePlayer() {
        try {
            startResumeUsersPlaybackRequest.execute();

            return new ResponseEntity<String>("Player resumed.", null, HttpStatus.OK);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ExpiredJwtException expired) {
            logger.info("Security exception: " + expired.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token Expired");
        }

        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> getPlayerInformation() {
        try {
            final CurrentlyPlayingContext currentlyPlayingContext = getInformationAboutUsersCurrentPlaybackRequest
                    .execute();

            System.out.println("Timestamp: " + currentlyPlayingContext.getTimestamp());
            return new ResponseEntity<String>(currentlyPlayingContext.toString(), null, HttpStatus.OK);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ExpiredJwtException expired) {
            logger.info("Security exception: " + expired.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token Expired");
        }

        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> setPlayerVolume(int volumePercent) {
        setVolumeForUsersPlaybackRequest = spotifyApi
                .setVolumeForUsersPlayback(volumePercent)
                .build();
        try {
            setVolumeForUsersPlaybackRequest.execute();

            return new ResponseEntity<String>("Volume changed to " + volumePercent + "%", null, HttpStatus.OK);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ExpiredJwtException expired) {
            logger.info("Security exception: " + expired.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token Expired");
        }

        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<?> getUserPlaylists() {
        try {
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest.execute();

            System.out.println("Total: " + playlistSimplifiedPaging.getTotal());
            System.out.println("URL: " + playlistSimplifiedPaging.getHref());


            

            return new ResponseEntity<String>(playlistSimplifiedPaging.getItems().toString(), null, HttpStatus.OK);

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ExpiredJwtException expired) {
            logger.info("Security exception: " + expired.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Token Expired");
        }

        return new ResponseEntity<String>("Error", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
