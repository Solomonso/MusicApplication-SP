package com.example.musicapplication_sp.interfaces

import com.example.musicapplication_sp.model.PostModel
import retrofit2.Call
import retrofit2.http.GET

interface SonglistApiService {
    @GET("/api")
    fun getPosts(): Call<MutableList<PostModel>>
}