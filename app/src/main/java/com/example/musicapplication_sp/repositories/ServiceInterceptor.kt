package com.example.musicapplication_sp.repositories

import okhttp3.Interceptor
import okhttp3.Response

class ServiceInterceptor: Interceptor {
    init{
        System.loadLibrary("keys")
    }
    private external fun getTokenKey(): String
    var token : String = getTokenKey()

//    fun Token(token: String ) {
//        this.token = token;
//    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if(request.header("No-Authentication")==null){
            //val token = getTokenFromSharedPreference();
            //or use Token Function
            if(!token.isNullOrEmpty())
            {
                val finalToken = "jwt $token"
                request = request.newBuilder()
                    .addHeader("Authorization",finalToken)
                    .build()
            }

        }

        return chain.proceed(request)
    }
}