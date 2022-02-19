package com.example.musicapplication_sp.model;

public enum Endpoints {
    GETUSERPLAYLISTS("https://api.spotify.com/v1/me/playlists"),
    CREATEPLAYLIST("https://api.spotify.com/v1/users/%s/playlists"),
    GETSONGFROMPLAYLIST("https://api.spotify.com/v1/playlists/%s/tracks"),
    ADDTOPLAYLIST("https://api.spotify.com/v1/playlists/%s/tracks"),
    USER("https://api.spotify.com/v1/me"),
    MUSICSONGS("https://musicapi.duckdns.org/api/songs/"),
    POSTCLIENTID("https://musicapi.duckdns.org/api/clientId"),
    GETCLIENTID("https://musicapi.duckdns.org/api/clientId/%UserID");

    private final String endpoints;

    Endpoints(String endpoints) {
        this.endpoints = endpoints;
    }

    public String getEndpoint() {
        return this.endpoints;
    }
}
