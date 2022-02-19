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

    @Headers("Accept:application/json",
        "Content-Type:application/json",
        "Authorization: jwt eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDQ1NzY0OTZ9.lxG2IoubFbjv7pPNpq0-8U5gHNSlmeUfIvSE_1uBjIc")
    @GET("songs/user/{UserId}")
    fun getSongsById(@Path(value = "UserId") UserId: String): Call<SongResponse>

    @GET("songs")
    fun getSongs(@Header("Authorization: ") authorization: String): Call<SongResponse>

    @Headers("Accept:application/json",
        "Content-Type:application/json",
        "Authorization: jwt {token}")
    @POST("songs")
    fun postSongs(@Body params: PostSongsModel): Call<PostSongsModel>

    @DELETE("songs")
    fun deleteSongs(): Call<SongResponse>

    @PATCH("songs")
    fun updateSongs(): Call<SongResponse>
}