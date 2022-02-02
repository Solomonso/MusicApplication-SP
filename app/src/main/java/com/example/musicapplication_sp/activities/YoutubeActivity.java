package com.example.musicapplication_sp.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;


import com.example.musicapplication_sp.R;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistSnippet;
import com.google.api.services.youtube.model.PlaylistStatus;

import java.util.Collection;
import java.util.Collections;

public class YoutubeActivity extends AppCompatActivity {
    private GoogleAccountCredential mCredential;
//
//    private static final String PREF_ACCOUNT_NAME = "accountName";
//    private static final int REQUEST_ACCOUNT_PICKER = 1000;
//    private static final int REQUEST_AUTHORIZATION = 1001;
//    private static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
//    private static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
//    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY, YouTubeScopes.YOUTUBE, YouTubeScopes.YOUTUBE_UPLOAD};
    private static final String CLIENT_SECRETS= "D:\\Files\\SP\\MusicApplicationSP\\app\\client_secret.json";
    private static final Collection<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/youtube.force-ssl");

    private static final String APPLICATION_NAME = "API code samples";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        this.initializeYoutubeApi();
        Log.d("Test", "Test");
    }

    public static Credential authorize(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        InputStream in = YoutubeActivity.class.getResourceAsStream(CLIENT_SECRETS);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                        .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static YouTube getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = authorize(httpTransport);
        return new YouTube.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public void initializeYoutubeApi() {
        try {
            YouTube youtubeService = getService();
            Playlist playlist = new Playlist();

            // Add the snippet object property to the Playlist object.
            PlaylistSnippet snippet = new PlaylistSnippet();
            snippet.setDefaultLanguage("en");
            snippet.setDescription("This is a sample playlist description.");
            snippet.setTitle("Music app playlist 2");
            playlist.setSnippet(snippet);

            // Add the status object property to the Playlist object.
            PlaylistStatus status = new PlaylistStatus();
            status.setPrivacyStatus("private");
            playlist.setStatus(status);

            // Define and execute the API request
            YouTube.Playlists.Insert request = youtubeService.playlists().insert("snippet,status", playlist);
            Playlist response = request.execute();
            Log.d("response ", String.valueOf(response));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}