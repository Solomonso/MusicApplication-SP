package com.example.musicapplication_sp.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import com.example.musicapplication_sp.adaptermodel.YoutubePlaylistItemAdapter
import com.example.musicapplication_sp.interfaces.OnYoutubePlaylistItemClickListener
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.ResourceId
import com.example.musicapplication_sp.model.VideoSnippet
import com.example.musicapplication_sp.model.YtPlaylistItem
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class YoutubePlaylistItemActivity : AppCompatActivity() {
    private val mJsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
    private val mTransport = AndroidHttp.newCompatibleTransport()
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlistsItems: ArrayList<YtPlaylistItem>
    private lateinit var mYoutubeDataApi: YouTube
    private lateinit var requestQueue: RequestQueue
    private lateinit var playlistId: String
    init {
        System.loadLibrary("keys")
    }
    private external fun getTokenKey(): String
    var token: String = getTokenKey()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtubeitemlist)
        recyclerView = findViewById(R.id.list_of_items)
        recyclerView.layoutManager = LinearLayoutManager(this)
        playlistsItems = arrayListOf()
        requestQueue = Volley.newRequestQueue(this)
        playlistId = intent.getStringExtra("playlist_id").toString()

        retrieveData()

        mYoutubeDataApi = YouTube.Builder(mTransport, mJsonFactory, null)
            .setApplicationName(resources.getString(R.string.app_name))
            .build()


        val adapter = YoutubePlaylistItemAdapter(playlistsItems)
        recyclerView.adapter = adapter
        //retrievePlaylist(ids, adapter)
        adapter.notifyItemInserted(1)
    }

    private fun getData(callBack: VolleyCallBack) {
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET,
            "https://www.googleapis.com/youtube/v3/playlistItems?key=$token&part=snippet&playlistId=$playlistId",
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
                                YtPlaylistItem::class.java
                            )
                        jsonObject = jsonObject?.optJSONObject("snippet")
                        assert(jsonObject != null)
                        val snippet = gson.fromJson(jsonObject.toString(), VideoSnippet::class.java)
                        playlist.snippet = snippet
                        jsonObject = jsonObject?.optJSONObject("resourceId")
                        assert(jsonObject != null)
                        val resourceId =
                            gson.fromJson(jsonObject.toString(), ResourceId::class.java)
                        playlist.resourceId = resourceId

                        playlistsItems.add(playlist)

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
                val adapter = YoutubePlaylistItemAdapter(playlistsItems)

                recyclerView.adapter = adapter
                adapter.setOnPlaylistClickListener(object : OnYoutubePlaylistItemClickListener {
                    override fun onItemClick(videoId: String?) {
                        val appIntent =
                            Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$videoId"))
                        val webIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=$videoId")
                        )
                        try {
                            startActivity(appIntent)
                        } catch (ex: ActivityNotFoundException) {
                            startActivity(webIntent)
                        }
                    }
                })
            }
        })
    }

}