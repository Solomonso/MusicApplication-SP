package com.example.musicapplication_sp.repositories;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.musicapplication_sp.interfaces.VolleyCallBack;
import com.example.musicapplication_sp.model.Endpoints;
import com.example.musicapplication_sp.model.Playlist;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlaylistService {
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private ArrayList<Playlist> playlists;

    public PlaylistService(RequestQueue queue, SharedPreferences sReferences) {
        this.requestQueue = queue;
        this.sharedPreferences = sReferences;
        this.playlists = new ArrayList<>();
    }

    public ArrayList<Playlist> getPlaylist() {
        return this.playlists;
    }

    public void getCurrentUserPlaylist(final VolleyCallBack callBack) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Endpoints.GETUSERPLAYLISTS.getEndpoint(), null,
                response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Playlist playlist = gson.fromJson(jsonObject.toString(), Playlist.class);
                            playlists.add(playlist);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                },
                error -> getCurrentUserPlaylist(() -> Log.d("Error", "Unable get current user playlist " + error))) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void createPlaylist(String id, String name) {
        final HashMap<String, String> postParams = new HashMap<>();
        postParams.put("name", name);

        String url = String.format(Endpoints.CREATEPLAYLIST.getEndpoint(), id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParams), response -> {

        }, error -> {
            if (postParams.isEmpty()) {
                Log.d("Error", "Unable to post");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}
