package com.example.musicapplication_sp.repositories;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.musicapplication_sp.interfaces.VolleyCallBack;
import com.example.musicapplication_sp.model.Endpoints;
import com.example.musicapplication_sp.model.UserSongsModel;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StoredUserSongsService {
    public native String getKey();
    static {
        System.loadLibrary("keys");
    }

    private RequestQueue requestQueue;
    private ArrayList<UserSongsModel> listOfSongs;

    public StoredUserSongsService(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        this.listOfSongs = new ArrayList<>();
    }

    public ArrayList<UserSongsModel> getListOfSongs()
    {
        return this.listOfSongs;
    }

    public void getCurrentUserPlaylist(String userId, final VolleyCallBack callBack) {
        String endpoint = String.format(Endpoints.GETSONGSFROMAPI.getEndpoint(), userId); //format the url to get the playlist id
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, endpoint, null,
                response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("data");
                    for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            UserSongsModel usersongs = gson.fromJson(jsonObject.toString(), UserSongsModel.class);
                            listOfSongs.add(usersongs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                },
                error -> Log.d("Error", "Unable get current user songs " + error)) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "jwt " + getKey();
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void saveTheSong(String userId, String clientId) {
        final HashMap<String, String> postParams = new HashMap<>();
        postParams.put("UserID", userId);
        postParams.put("ClientID", clientId);

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
        requestQueue.add(jsonObjectRequest);
    }
}
