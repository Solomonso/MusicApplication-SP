package com.example.musicapplication_sp.interfaces

interface PlaylistUpdateDelete {
    fun modifyItem(itemUID :String, isDone : Boolean)
    fun onItemDelete(itemUID: String)
}