package com.example.musicapplication_sp.model;

public class UserSongsModel {
    private String UserID;
    private String songName;

    public UserSongsModel(String userID, String songName)
    {
        UserID = userID;
        this.songName = songName;
    }

    public String getUserID()
    {
        return UserID;
    }

    public void setUserID(String userID)
    {
        UserID = userID;
    }

    public String getSongName()
    {
        return songName;
    }

    public void setSongName(String songName)
    {
        this.songName = songName;
    }

    @Override
    public String toString() {
        return "UserSongsModel{" +
                "UserID='" + UserID + '\'' +
                ", songName='" + songName + '\'' +
                '}';
    }
}
