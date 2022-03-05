package com.example.musicapplication_sp.repositories

import com.example.musicapplication_sp.interfaces.SonglistCrudMethod
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

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
                var client = OkHttpClient.Builder()
                    .addInterceptor(ServiceInterceptor())
                    .build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!
        }
    }
}