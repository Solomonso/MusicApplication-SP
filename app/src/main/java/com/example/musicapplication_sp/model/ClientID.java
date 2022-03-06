package com.example.musicapplication_sp.model;

public class ClientID {
    private String clientID;
    public String iv;

    public ClientID(String clientID, String iv) {
        this.clientID = clientID;
        this.iv = iv;
    }

    public String getClientId() {
        return clientID;
    }
    public void setClientID(String clientId) {
        this.clientID = clientId;
    }

    public String getIv() {
        return this.iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

}
