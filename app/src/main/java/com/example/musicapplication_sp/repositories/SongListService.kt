package com.example.musicapplication_sp.repositories

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class SongListService {
    companion object {
        init {
            System.loadLibrary("keys")
        }

        private var retrofit: Retrofit? = null
        private external fun getURL(): String
        private var BASE_URL: String = getURL()

        fun getInstance(): Retrofit {
            if (retrofit == null) {


                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}