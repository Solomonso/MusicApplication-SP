package com.example.musicapplication_sp.interfaces

import com.example.musicapplication_sp.data.SongResponse
import com.example.musicapplication_sp.model.PostSongsModel
import retrofit2.Call
import retrofit2.http.*


interface SonglistCrudMethod {
    @Headers("Authorization")
    @GET("songs/user/{UserId}")
    fun getSongsById(@Path(value = "UserId") UserId: String?): Call<SongResponse?>?

    //    @Headers("No-Authentication: true")
    @GET("songs")
    fun getSongs(@Header("Authorization") auth: String): Call<SongResponse>

    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
        "Authorization: jwt eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJib2R5Ijoic3R1ZmYiLCJpYXQiOjE2NDQ1NzY0OTZ9.lxG2IoubFbjv7pPNpq0-8U5gHNSlmeUfIvSE_1uBjIc"
    )
    @POST("songs")
    fun postSongs(@Body params: PostSongsModel): Call<PostSongsModel>

    @DELETE("songs")
    fun deleteSongs(): Call<SongResponse>

    @PATCH("songs")
    fun updateSongs(): Call<SongResponse>
}