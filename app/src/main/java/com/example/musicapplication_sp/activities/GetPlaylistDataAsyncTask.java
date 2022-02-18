package com.example.musicapplication_sp.activities;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.io.IOException;

public class GetPlaylistDataAsyncTask extends AsyncTask<String[], Void, PlaylistListResponse> {
    private static final String YOUTUBE_PLAYLIST_PART = "snippet";
    private static final String YOUTUBE_PLAYLIST_FIELDS = "items(id,snippet(title))";

    private YouTube mYouTubeDataApi;

    public GetPlaylistDataAsyncTask(YouTube api) {
        mYouTubeDataApi = api;
    }

    @Override
    protected PlaylistListResponse doInBackground(String[]... params) {

        final String[] playlistIds = params[0];

        PlaylistListResponse playlistListResponse;
        try {
            playlistListResponse = mYouTubeDataApi.playlists()
                    .list(YOUTUBE_PLAYLIST_PART)
                    .setId(TextUtils.join(",", playlistIds))
                    .setFields(YOUTUBE_PLAYLIST_FIELDS)
                    .setKey("AIzaSyDGDhdiqacmjroaO7-Bar_fgP6G2YVEHsA") //Here you will have to provide the keys
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            YouTube.PlaylistItems.List request = mYouTubeDataApi.playlistItems()
                    .list("snippet")
                    .setPlaylistId("RDaGSKrC7dGcY")
                    .setKey("AIzaSyDGDhdiqacmjroaO7-Bar_fgP6G2YVEHsA");
            PlaylistItemListResponse response = request.execute();
            System.out.println(response);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return playlistListResponse;
    }

    protected void UserPlaylists() throws IOException {
        YouTube.Channels.List request = mYouTubeDataApi.channels().list("id");
        ChannelListResponse response = request.setForUsername("GoogleDevelopers").execute();
        System.out.println(response);
    }
}
