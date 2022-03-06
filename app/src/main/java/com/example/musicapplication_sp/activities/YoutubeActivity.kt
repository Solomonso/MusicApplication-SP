package com.example.musicapplication_sp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.YoutubePlaylistAdapter
import com.example.musicapplication_sp.interfaces.OnYoutubePlaylistClickListener
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.Snippet
import com.example.musicapplication_sp.model.YtPlaylist
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class YoutubeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlists: ArrayList<YtPlaylist>
    private lateinit var requestQueue: RequestQueue

    init {
        System.loadLibrary("keys")
    }

    private external fun getTokenKey(): String
    var token: String = getTokenKey()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ytplaylist)
        recyclerView = findViewById(R.id.itemPlaylist)
        recyclerView.layoutManager = LinearLayoutManager(this)
        playlists = arrayListOf()
        requestQueue = Volley.newRequestQueue(this)


        retrieveData()

        val adapter = YoutubePlaylistAdapter(playlists)
        recyclerView.adapter = adapter
        adapter.notifyItemInserted(1)
    }

    private fun getData(callBack: VolleyCallBack) {

        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            "https://www.googleapis.com/youtube/v3/playlists?key=$token&part=snippet&id=PLC1og_v3eb4hrv4wsqG1G5dsNZh9bIscJ,PL3EfCK9aCbkptFjtgWYJ8wiXgJQw5k3M3,PLOghHB6dKHAISSUl-IoUpE0KAg29mrdad",
            null,
            Response.Listener { response: JSONObject ->
                val gson = Gson()
                val jsonArray = response.optJSONArray("items")
                for (i in 0 until Objects.requireNonNull(jsonArray).length()) {
                    try {
                        var jsonObject = jsonArray?.getJSONObject(i)

                        val playlist =
                            gson.fromJson(
                                jsonObject.toString(),
                                YtPlaylist::class.java
                            )
                        jsonObject = jsonObject?.optJSONObject("snippet")
                        assert(jsonObject != null)
                        val snippet = gson.fromJson(jsonObject.toString(), Snippet::class.java)
                        playlist.snippet = snippet
                        playlists.add(playlist)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                callBack.onSuccess()
            },
            Response.ErrorListener { error: VolleyError ->
                Log.d(
                    "Error",
                    "Unable get song from playlist $error"
                )
            }) {

        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun retrieveData() {

        getData(object : VolleyCallBack {
            override fun onSuccess() {
                val adapter = YoutubePlaylistAdapter(playlists)

                recyclerView.adapter = adapter
                adapter.setOnPlaylistClickListener(object : OnYoutubePlaylistClickListener {
                    override fun onItemClick(playlistId: String?) {
                        val youtubePlaylistItemintent =
                            Intent(this@YoutubeActivity, YoutubePlaylistItemActivity::class.java)
                        youtubePlaylistItemintent.putExtra("playlist_id", playlistId)
                        startActivity(youtubePlaylistItemintent)
                    }
                })
            }
        })
    }

}