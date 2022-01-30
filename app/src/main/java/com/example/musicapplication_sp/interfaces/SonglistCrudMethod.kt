package com.example.musicapplication_sp.interfaces

import com.example.musicapplication_sp.data.SongResponse
import retrofit2.Call
import retrofit2.http.*

interface SonglistCrudMethod {
    // GET all data from songs root
    @Headers("Authorization: jwt eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDMzMTMzNDl9.fQEgE7ZdN8o8yDu40GSR_o0iQ2hfjWdugTuI-wpWZHI")
    @GET("songs")
    fun getSongs(): Call<SongResponse>

    @POST("songs")
    fun postSongs(): Call<SongResponse>

    @DELETE("songs")
    fun deleteSongs(): Call<SongResponse>

    @PATCH("songs")
    fun updateSongs(): Call<SongResponse>
}