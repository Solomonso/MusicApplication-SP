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
import com.example.musicapplication_sp.R;
import com.example.musicapplication_sp.cryptography.Cryptography;
import com.example.musicapplication_sp.model.ClientID;
import com.example.musicapplication_sp.model.Endpoints;
import com.example.musicapplication_sp.model.User;
import com.example.musicapplication_sp.repositories.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SpotifyLoginActivity extends AppCompatActivity {


    public SpotifyLoginActivity() throws GeneralSecurityException, IOException {
    }

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
    private ArrayList<ClientID> clientIDs;
    private ClientIDService clientIDService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify);
        spotifyClientID = findViewById(R.id.text_spotify_client_id);
        auth = FirebaseAuth.getInstance();
        authorizeAccessButton = findViewById(R.id.authorize_access);
        getTheClientID("1234");
        authSpotify();
        this.clientIDs = new ArrayList<>();
        this.clientIDService(rQueue, sharedPreferences);
//        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE);

//        try {
//            MasterKey masterKey = new MasterKey.Builder(this)
//                    .setKeyScheme(androidx.security.crypto.MasterKey.KeyScheme.AES256_GCM).build();
//
//            sharedPreferences = EncryptedSharedPreferences.create(
//                    SpotifyLoginActivity.this,
//                    "Spotify",
//                    masterKey,
//                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//                    );
//        } catch (GeneralSecurityException | IOException e) {
//            e.printStackTrace();
//        }

    }

    public ArrayList<ClientID> getClientIDs() {
        return this.clientIDs;
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
                editor.apply();

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
                    try {
                        sharedPreferences = getEncryptedSharedPreferences();
                    } catch (GeneralSecurityException | IOException e) {
                        e.printStackTrace();
                    }
                    editor = sharedPreferences.edit();
                    editor.putString("token", response.getAccessToken());
                    editor.apply();
//                    editor = getSharedPreferences("Spotify", MODE_PRIVATE).edit();
//                    editor.putString("token", response.getAccessToken());
//                    editor.commit();
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
            try {
                sharedPreferences = getEncryptedSharedPreferences();
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
            editor = sharedPreferences.edit();
            editor.putString("username", user.display_name);
            editor.apply();
//            editor = getSharedPreferences("Spotify", 0).edit();
//            editor.putString("username", user.display_name);
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

    public void getTheClientID(String UserID, final VolleyCallBack callBack) {
        String endpoint = String.format(Endpoints.GETCLIENTID.getEndpoint(), UserID); //format the url to get the playlist id
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null,
                response -> {
                    JSONArray jsonArray = response.optJSONArray("data");
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                        try {
                            Gson gson = new Gson();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ClientID clientID = gson.fromJson(jsonObject.toString(), ClientID.class);
                            clientIDs.add(clientID);
                            Toast.makeText(this, "id: " + jsonObject.getString("id") + " client id " + jsonObject.getString("ClientID") + " iv " + jsonObject.getString("iv"), Toast.LENGTH_SHORT).show();

                            String client_id = response.getString("ClientID");
                            String iv = response.getString("iv");
                            Toast.makeText(this, "client id " + client_id + " iv " + iv, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "client id " + clientID.getClientId() + " iv " + clientID.getIv(), Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                    //Toast.makeText(this,"Response: " + response, Toast.LENGTH_SHORT).show();

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