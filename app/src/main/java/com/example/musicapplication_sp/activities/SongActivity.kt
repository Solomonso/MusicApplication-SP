package com.example.musicapplication_sp.activities

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.SongAdapter
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.Song
import com.example.musicapplication_sp.repositories.SongService

class SongActivity : AppCompatActivity(){
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var requestQueue: RequestQueue
    private lateinit var songs: ArrayList<Song>
    private lateinit var songService: SongService
    private lateinit var playlistId: String
    private lateinit var recyclerView: RecyclerView

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
        retrieveSongFromPlaylist()

    }

    private fun retrieveSongFromPlaylist() {
        songService.getSongsFromCurrentPlaylist(playlistId, object : VolleyCallBack {
            override fun onSuccess() {
                val song = songService.songs
                for (s in song) {
                    songs.add(s)
                }
                val adapter = SongAdapter(songs)
                recyclerView.adapter = adapter
            }
        })
    }
}