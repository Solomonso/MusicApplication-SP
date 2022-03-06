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
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicapplication_sp.R;
import com.example.musicapplication_sp.cryptography.Cryptography;
import com.example.musicapplication_sp.interfaces.VolleyCallBack;
import com.example.musicapplication_sp.model.ClientID;
import com.example.musicapplication_sp.model.Endpoints;
import com.example.musicapplication_sp.model.User;
import com.example.musicapplication_sp.repositories.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class SpotifyLoginActivity extends AppCompatActivity {

    public native String getKey();
    static {
        System.loadLibrary("keys");
    }
    private static final int REQUEST_CODE = 1337;
    private static final String REDIRECT_URI = "https://com.example.musicapplication_sp//callback";
    private SharedPreferences sharedPreferences;
    private RequestQueue rQueue;
    private SharedPreferences.Editor editor;
    private EditText spotifyClientID;
    private Button authorizeAccessButton;
    private  FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        spotifyClientID = findViewById(R.id.text_spotify_client_id);
        auth = FirebaseAuth.getInstance();
        authorizeAccessButton = findViewById(R.id.authorize_access);
        authSpotify();
        getTheClientID("1234");
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE);
    }

    public MasterKey getMasterKey() throws GeneralSecurityException, IOException {
        return new MasterKey.Builder(this)
                .setKeyScheme(androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM).build();
    }

    public SharedPreferences getEncryptedSharedPreferences() throws GeneralSecurityException, IOException {
        return EncryptedSharedPreferences.create(
                SpotifyLoginActivity.this,
                "Spotify",
                getMasterKey(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    /**
     * Uses the Spotify Authentication Library,.
     * Which is used for signing in and handles the authentication flow and Spotify Web API calls
     */
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
                Cryptography cryptography = new Cryptography();
                cryptography.createSecretKey("AES");
                byte[] byteArray = CLIENT_ID.getBytes(StandardCharsets.UTF_8);
                HashMap<String, byte[]> encryptedMap = (HashMap<String, byte[]>) cryptography.encrypt(byteArray);

                String encryptedString = new String(encryptedMap.get("encrypted"), StandardCharsets.UTF_8);
                String iv = new String(encryptedMap.get("iv"));
                saveTheClientID(auth.getUid(), encryptedString, iv);
                editor = getSharedPreferences("Spotify", MODE_PRIVATE).edit();
                editor.putString("client_id", CLIENT_ID);
                editor.commit();

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
        Intent intent = new Intent(SpotifyLoginActivity.this, SettingActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Spotify Account linked successfully.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void saveTheClientID(String userId, String clientId, String iv) {
        final HashMap<String, String> postParams = new HashMap<>();
        postParams.put("UserID", userId);
        postParams.put("ClientID", clientId);
        postParams.put("iv", iv);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Endpoints.POSTCLIENTID.getEndpoint(), new JSONObject(postParams), response -> {

        }, error -> {
            if (postParams.isEmpty()) {
                Log.d("Error", "Unable to post");
            }
        }) {
        @Override
        public Map<String, String> getHeaders() {
            Map<String, String> headers = new HashMap<>();
            String auth = "jwt " + getKey();
            headers.put("Authorization", auth);
            headers.put("Content-Type", "application/json");
            return headers;
        }
    };
        rQueue.add(jsonObjectRequest);
    }

        public void getTheClientID(String UserID) {
        String endpoint = String.format(Endpoints.GETCLIENTID.getEndpoint(), UserID); //format the url to get the playlist id
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null,
                response -> {
                    JSONArray jsonArray = response.optJSONArray("data");
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Log.d("Test ", " client id " + jsonObject.getString("ClientID") + " iv " + Arrays.toString(jsonObject.getString("iv").getBytes(StandardCharsets.UTF_8)));
                            Cryptography cryptography = new Cryptography();
                            cryptography.createSecretKey("AES");
                            HashMap<String, byte[]> map = new HashMap<>();
                            map.put(Arrays.toString(jsonObject.getString("iv").getBytes(StandardCharsets.UTF_8)), jsonObject.getString("ClientID").getBytes(StandardCharsets.UTF_8));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.d("Error", "Unable get client id " + error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "jwt " + getKey();
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        rQueue = Volley.newRequestQueue(this);
        rQueue.add(jsonObjectRequest);
    }
}