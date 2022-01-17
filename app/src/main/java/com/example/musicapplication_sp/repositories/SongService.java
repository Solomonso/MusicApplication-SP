package com.example.musicapplication_sp.repositories;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.musicapplication_sp.interfaces.VolleyCallBack;
import com.example.musicapplication_sp.model.Endpoints;
import com.example.musicapplication_sp.model.Song;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SongService {
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private ArrayList<Song> songs;
    public SongService(RequestQueue queue, SharedPreferences sPreferences) {
        this.requestQueue = queue;
        this.sharedPreferences = sPreferences;
        this.songs = new ArrayList<>();
    }

    public ArrayList<Song> getSongs() {
        return this.songs;
    }

    public void getSongsFromCurrentPlaylist(String playlistId, final VolleyCallBack callBack) {
        String endpoint = String.format(Endpoints.GETSONGFROMPLAYLIST.getEndpoint(), playlistId); //format the url to get the playlist id
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,  endpoint,null,
                response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            jsonObject = jsonObject.optJSONObject("track");
                            assert jsonObject != null;
                            Song song = gson.fromJson(jsonObject.toString(), Song.class);
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess(); },
                error ->  Log.d("Error", "Unable get song from playlist " + error)) {
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
