package com.example.musicapplication_sp

class PlaylistModel {
    companion object Factory {
        fun createList(): PlaylistModel = PlaylistModel()
    }

    var UID: String? = null
    var itemDatatext: String? = null
    var delete: Boolean? = false
}