package com.example.musicapplication_sp.interfaces

import com.example.musicapplication_sp.data.SongResponse
import com.example.musicapplication_sp.model.PostSongsModel
import com.example.musicapplication_sp.repositories.SongListService
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.http.*

interface SonglistCrudMethod {
    companion object {
        init {
            System.loadLibrary("keys")
        }

        private external fun getTokenKey(): String
        private var token: String = getTokenKey()
    }

    @GET("songs/user/{UserId}")
    fun getSongsById(
        @Path("UserId") id: String,
        @Header("Authorization") token: String
    ): Call<SongResponse>

    @GET("songs")
    fun getSongs(): Call<SongResponse>


    @POST("songs")
    fun postSongs(
        @Body params: PostSongsModel,
        @Header("Authorization") token: String
    ): Call<PostSongsModel>

    @DELETE("songs")
    fun deleteSongs(): Call<SongResponse>

    @PATCH("songs")
    fun updateSongs(): Call<SongResponse>
}