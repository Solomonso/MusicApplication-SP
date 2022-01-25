package com.example.musicapplication_sp.interfaces

import com.example.musicapplication_sp.model.SongResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface SonglistCrudMethod {
    // GET all data from songs root
    @GET("songs")
    fun getSongs(): Call<SongResponse>

    @POST("songs")
    fun postSongs(): Call<SongResponse>

    @DELETE("songs")
    fun deleteSongs(): Call<SongResponse>

    @PATCH("songs")
    fun updateSongs(): Call<SongResponse>
}