package com.example.musicapplication_sp.interfaces

import com.example.musicapplication_sp.data.SongResponse
import com.example.musicapplication_sp.model.PostSongsModel
import retrofit2.Call
import retrofit2.http.*

interface SonglistCrudMethod {

    @GET("songs/user/{UserID}")
    fun getSongsById(
        @Path("UserID") id: String,
        @Header("Authorization") token: String
    ): Call<SongResponse>

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