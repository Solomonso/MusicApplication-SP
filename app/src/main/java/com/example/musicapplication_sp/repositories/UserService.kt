package com.example.musicapplication_sp.repositories

import android.content.SharedPreferences
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.example.musicapplication_sp.model.User
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.VolleyError
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.Endpoints

import com.google.gson.Gson
import org.json.JSONObject

class UserService(requestQueue: RequestQueue, sharedPreferences: SharedPreferences) {
    private var sPreferences: SharedPreferences = sharedPreferences
    private var rQueue: RequestQueue = requestQueue
    private lateinit var user: User
    fun getUser(): User {
        return user
    }

    fun get(callBack: VolleyCallBack) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Endpoints.USER.endpoint, null,
            Response.Listener { response: JSONObject ->
                val gson = Gson()
                user = gson.fromJson(
                    response.toString(),
                    User::class.java
                )
                callBack.onSuccess()
            },
            Response.ErrorListener { error: VolleyError? ->  }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                val token: String? = sPreferences.getString("token", "")
                val auth = "Bearer $token"
                headers["Authorization"] = auth
                return headers
            }
        }
        rQueue.add(jsonObjectRequest)
    }


}