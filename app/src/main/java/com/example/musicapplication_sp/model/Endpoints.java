package com.example.musicapplication_sp.model;

public enum Endpoints {
    PLAYLIST("https://api.spotify.com/v1/users/%s/playlists"),
    ADDTOPLAYLIST("https://api.spotify.com/v1/playlists/{playlist_id}/tracks"),
    USER("https://api.spotify.com/v1/me");
    private final String endpoints;
    Endpoints(String endpoints) {
        this.endpoints = endpoints;
    }

    public String getEndpoint() {
        return this.endpoints;
    }
}
