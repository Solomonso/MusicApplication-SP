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
        private external fun getTokenKey(): String
        var token: String = getTokenKey()

//    fun create(): SonglistCrudMethod {
//            val client = OkHttpClient.Builder()
//                .addInterceptor { chain -> return@addInterceptor addApiKeyToRequests(chain) }
//                .build()
//            return Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .client(client)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build()
//                .create(SonglistCrudMethod::class.java)
//        }
//
//    private fun addApiKeyToRequests(chain: Interceptor.Chain): Response {
//        val request = chain.request().newBuilder()
//        val originalHttpUrl = chain.request().url()
//        val newUrl = originalHttpUrl.newBuilder()
//            .addQueryParameter("Authorization: ", " jwt $token").build()
//        request.url(newUrl)
//        return chain.proceed(request.build())
//    }

//        fun getInstance(): Retrofit {
////            var client = OkHttpClient.Builder().addInterceptor { chain ->
////                val newRequest: Request = chain.request().newBuilder()
////                    .addHeader("Authorization", "jwt $token")
////                    .build()
////                chain.proceed(newRequest)
////            }.build()
//            if (retrofit == null) {
//                retrofit = Retrofit.Builder()
////                    .client()
//                    .baseUrl(BASE_URL)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
//            }
//            return retrofit!!
//        }

        fun getInstance(): Retrofit {
            if (retrofit == null) {
                var client = OkHttpClient.Builder()
                    .addInterceptor(ServiceInterceptor())
                    //.readTimeout(45,TimeUnit.SECONDS)
                    //.writeTimeout(45,TimeUnit.SECONDS)
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