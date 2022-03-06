package com.example.musicapplication_sp.repositories

import okhttp3.Interceptor
import okhttp3.Response

class ServiceInterceptor : Interceptor{

    init{
        System.loadLibrary("keys")
    }

    private external fun getTokenKey(): String
    private var token : String = getTokenKey()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if(request.headers("No-Authentication")==null){
            if(!token.isNullOrEmpty())
            {
                val finalToken =  "jwt " + token
                request = request.newBuilder()
                    .addHeader("Authorization: ",finalToken)
                    .build()
            }

        }

        return chain.proceed(request)
    }
//
////    private fun addApiKeyToRequests(chain: Interceptor.Chain): Response {
////        val request = chain.request().newBuilder()
////        val originalHttpUrl = chain.request().url
////        val newUrl = originalHttpUrl.newBuilder()
////            .addQueryParameter("Authorization:", " jwt $token").build()
////        request.url(newUrl)
////        return chain.proceed(request.build())
////    }
}