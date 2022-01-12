package com.example.musicapplication_sp.repositories;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.musicapplication_sp.model.Endpoints;
import com.example.musicapplication_sp.model.Playlist;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.squareup.okhttp.MediaType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaylistService {
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private ArrayList<Playlist> playlist;
    private static final MediaType JSON = MediaType.parse("application/json; charset-utf-8");
    public PlaylistService(RequestQueue queue, SharedPreferences sReferences) {
        this.requestQueue = queue;
        this.sharedPreferences = sReferences;
        this.playlist = new ArrayList<>();
    }

    public ArrayList<Playlist> getPlaylist() {
        return this.playlist;
    }

    public void createPlaylist(String id, String name) {
        final HashMap<String, String> postParams = new HashMap<>();
        postParams.put("name", name);

        String url = String.format(Endpoints.PLAYLIST.getEndpoint(), id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParams), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (postParams.isEmpty()) {
                    Log.d("Error", "Unable to post");
                }
            }
        }) {
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
}
