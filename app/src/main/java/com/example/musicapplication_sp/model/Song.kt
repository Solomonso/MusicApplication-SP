package com.example.musicapplication_sp.model

class Song {
     private var id: String
     private var songName: String

     constructor(id: String, name: String) {
        this.songName = name
        this.id = id
    }

    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getName(): String {
        return this.songName
    }

    fun setName(name: String) {
        this.songName = name
    }
}