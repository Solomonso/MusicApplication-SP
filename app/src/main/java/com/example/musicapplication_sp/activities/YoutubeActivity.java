package com.example.musicapplication_sp.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicapplication_sp.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistListResponse;

public class YoutubeActivity extends AppCompatActivity {
    private YouTube mYoutubeDataApi;
    private final JsonFactory mJsonFactory = JacksonFactory.getDefaultInstance();
    private final HttpTransport mTransport = AndroidHttp.newCompatibleTransport();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        mYoutubeDataApi = new YouTube.Builder(mTransport, mJsonFactory, null)
                .setApplicationName(getResources().getString(R.string.app_name))
                .build();
        String[] ids = {"RDaGSKrC7dGcY"};
                new GetPlaylistDataAsyncTask(mYoutubeDataApi) {

                }.execute(ids);
    }
    }

