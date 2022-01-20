package com.example.musicapplication_sp.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.SongAdapter
import com.example.musicapplication_sp.interfaces.OnSongClickListener
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.Song
import com.example.musicapplication_sp.repositories.SongService
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class SongActivity : AppCompatActivity(){
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var requestQueue: RequestQueue
    private lateinit var songs: ArrayList<Song>
    private lateinit var songService: SongService
    private lateinit var playlistId: String
    private lateinit var recyclerView: RecyclerView
    private var spotifyAppRemote: SpotifyAppRemote? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE)
        requestQueue = Volley.newRequestQueue(this)
        songService = SongService(requestQueue, sharedPreferences)
        playlistId = intent.getStringExtra("playlist_id").toString()
        recyclerView = findViewById(R.id.item_song)
        recyclerView.layoutManager = LinearLayoutManager(this)
        songs = arrayListOf()
    }

    /**
     * Connecting to the Spotify app remote which is used for only managing audio playback
     */
    override fun onStart() {
        super.onStart()
        val clientId = sharedPreferences.getString("client_id","")
        val redirectUri = "https://com.example.musicapplication_sp//callback"
        val connectionParams = ConnectionParams.Builder(clientId).setRedirectUri(redirectUri).showAuthView(true).build()

        SpotifyAppRemote.connect(this, connectionParams, object : Connector.ConnectionListener {
            override fun onConnected(appRemote: SpotifyAppRemote) {
                spotifyAppRemote = appRemote
                retrieveSongFromPlaylist()
            }

            override fun onFailure(throwable: Throwable) {
                Log.e(this@SongActivity.toString(), throwable.message, throwable)
            }
        })
    }

    private fun retrieveSongFromPlaylist() {
        songService.getSongsFromCurrentPlaylist(playlistId, object : VolleyCallBack {
            override fun onSuccess() {
                val song = songService.songs
                for (s in song) {
                    songs.add(s)
                    Log.d("track info ", s.uri)
                }
                val adapter = SongAdapter(songs)

                recyclerView.adapter = adapter
                adapter.setOnSongPlayListener(object : OnSongClickListener {
                    override fun onItemClick(position: Int) {

                    }
                })
            }
        })
    }
}