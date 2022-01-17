package com.example.musicapplication_sp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.musicapplication_sp.R;
import com.example.musicapplication_sp.model.User;
import com.example.musicapplication_sp.repositories.UserService;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class SpotifyLogin extends AppCompatActivity {
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "https://com.example.musicapplication_sp//callback";
    private SharedPreferences sharedPreferences;
    private RequestQueue rQueue;
    private SharedPreferences.Editor editor;
    private EditText spotifyClientID;
    private Button authorizeAccessButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_activity);
        spotifyClientID = findViewById(R.id.text_spotify_client_id);
        authorizeAccessButton = findViewById(R.id.authorize_access);
        authSpotify();
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE);
        rQueue = Volley.newRequestQueue(this);
    }

    private void authSpotify() {
        authorizeAccessButton.setOnClickListener(view -> {
            String CLIENT_ID = spotifyClientID.getText().toString();
            Log.d("id", CLIENT_ID);
            if (TextUtils.isEmpty(CLIENT_ID)) {
                Toast.makeText(this, "Enter client id", Toast.LENGTH_SHORT).show();
            } else {
                AuthorizationRequest.Builder builder = new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
                builder.setScopes(new String[]{"user-read-recently-played, user-read-currently-playing, playlist-modify-public, user-top-read, app-remote-control, user-read-playback-position, user-library-read, user-library-modify, user-modify-playback-state, user-read-playback-state, user-read-currently-playing, user-read-email,streaming, playlist-modify-private, playlist-read-private"});

                AuthorizationRequest request = builder.build();

                AuthorizationClient.openLoginActivity(this, REQUEST_CODE, request);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                case TOKEN:
                    editor = getSharedPreferences("Spotify", MODE_PRIVATE).edit();
                    editor.putString("token", response.getAccessToken());
                    editor.commit();
                    waitForUserInfo();
                    break;
                case ERROR:
                    Toast.makeText(this, "Failed to link account " + response.getError(), Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }

    private void waitForUserInfo() {
        UserService userService = new UserService(rQueue, sharedPreferences);
        userService.get(() -> {
            User user = userService.getUser();
            editor = getSharedPreferences("Spotify", 0).edit();
            editor.putString("username", user.display_name);
            Log.d("Starting", "Got user information");
            editor.commit();
            startMainActivity();
        });
    }

    private void startMainActivity() {
        Intent intent = new Intent(SpotifyLogin.this, SettingActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Spotify Account linked successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }
}