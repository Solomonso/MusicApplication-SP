package com.example.musicapplication_sp

interface PlaylistUpdateDelete {
    fun modifyItem(itemUID :String, isDone : Boolean)
    fun onItemDelete(itemUID: String)
}