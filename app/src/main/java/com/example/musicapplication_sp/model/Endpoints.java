package com.example.musicapplication_sp.model;

public enum Endpoints {
    GETUSERPLAYLISTS("https://api.spotify.com/v1/me/playlists"),
    GETSONGFROMPLAYLIST("https://api.spotify.com/v1/playlists/%s/tracks"),
    ADDTOPLAYLIST("https://api.spotify.com/v1/playlists/%s/tracks"),
    USER("https://api.spotify.com/v1/me");
    private final String endpoints;
    Endpoints(String endpoints) {
        this.endpoints = endpoints;
    }

    public String getEndpoint() {
        return this.endpoints;
    }
}
