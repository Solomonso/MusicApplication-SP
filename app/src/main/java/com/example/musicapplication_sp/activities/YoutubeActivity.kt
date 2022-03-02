package com.example.musicapplication_sp.activities

import androidx.appcompat.app.AppCompatActivity
import com.google.api.services.youtube.YouTube
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.extensions.android.http.AndroidHttp
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.model.YtPlaylist
import com.google.api.client.json.JsonFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class YoutubeActivity : AppCompatActivity() {
    private val mJsonFactory: JsonFactory = JacksonFactory.getDefaultInstance()
    private val mTransport = AndroidHttp.newCompatibleTransport()
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlists : ArrayList<YtPlaylist>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ytplaylist)
        recyclerView = findViewById(R.id.itemPlaylist)
        recyclerView.layoutManager = LinearLayoutManager(this)
       playlists = arrayListOf()
        retrieveUserPlaylist()

        var mYoutubeDataApi: YouTube = YouTube.Builder(mTransport, mJsonFactory, null)
            .setApplicationName(resources.getString(R.string.app_name))
            .build()
        val ids = arrayOf("RDMMBciS5krYL80")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var task : GetPlaylistDataAsyncTask? = mYoutubeDataApi?.let {
                    GetPlaylistDataAsyncTask(it)
                }
                if (task != null) {
                    var playlists = task.getPlaylists(ids)
                    for (item in playlists?.items!!){
                        var title = item.snippet.title
                        var channel = item.snippet.channelTitle
                        println(title)
                    }
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
    fun retrieveUserPlaylist(){

    }
}