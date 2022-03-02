package com.example.musicapplication_sp.activities

import android.text.TextUtils
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.model.PlaylistListResponse
import java.io.IOException

class GetPlaylistDataAsyncTask(private val mYouTubeDataApi: YouTube) {

    fun getPlaylists(vararg p0: Array<String>): PlaylistListResponse? {

        val playlistIds = p0[0]

        val playlistListResponse: PlaylistListResponse = try {
            mYouTubeDataApi.playlists()
                .list(YOUTUBE_PLAYLIST_PART)
                .setId(playlistIds?.let { TextUtils.join(",", it) })
                .setFields(YOUTUBE_PLAYLIST_FIELDS)
                .setKey("AIzaSyDGDhdiqacmjroaO7-Bar_fgP6G2YVEHsA") //Here you will have to provide the keys
                .execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        try {
            val request = mYouTubeDataApi.playlistItems()
                .list("snippet")
                .setPlaylistId("RDMMBciS5krYL80")
                .setKey("AIzaSyDGDhdiqacmjroaO7-Bar_fgP6G2YVEHsA")
            val response = request.execute()
            println(response)
            val test = response.items[1].snippet.title
            println(test)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return playlistListResponse
    }

    @Throws(IOException::class)
    fun UserPlaylists() {
        val request = mYouTubeDataApi.channels().list("id")
        val response = request.setForUsername("GoogleDevelopers").execute()
        println(response)
    }

    companion object {
        private const val YOUTUBE_PLAYLIST_PART = "snippet"
        private const val YOUTUBE_PLAYLIST_FIELDS = "items(id,snippet(title))"
    }
}