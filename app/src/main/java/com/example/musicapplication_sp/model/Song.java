package com.example.musicapplication_sp.model;

public class Song {
    private String id;
    private String name;
    private String artist;
    private String uri;

    public Song(String id, String name, String uri) {
        this.name = name;
        this.id = id;
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUri() {
        return this.uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
