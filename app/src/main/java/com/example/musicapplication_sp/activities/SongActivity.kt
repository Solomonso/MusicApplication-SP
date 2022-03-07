package com.example.musicapplication_sp.activities

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.SongAdapter
import com.example.musicapplication_sp.interfaces.OnSongClickListener
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.PlayingState
import com.example.musicapplication_sp.model.Song
import com.example.musicapplication_sp.repositories.SongService
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote

class SongActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var requestQueue: RequestQueue
    private lateinit var songs: ArrayList<Song>
    private lateinit var songService: SongService
    private lateinit var playlistId: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
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

        toolbar = findViewById(R.id.toolbarSong)
        this.toolbar()
    }

    /**
     * Connecting to the Spotify app remote which is used for only managing audio playback
     */
    override fun onStart() {
        super.onStart()
        val clientId = sharedPreferences.getString("client_id", "")
        val redirectUri = "https://com.example.musicapplication_sp//callback"
        val connectionParams =
            ConnectionParams.Builder(clientId).setRedirectUri(redirectUri).showAuthView(true)
                .build()

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
                }
                val adapter = SongAdapter(songs)

                recyclerView.adapter = adapter
                adapter.setOnSongPlayListener(object : OnSongClickListener {
                    override fun onItemClick(position: Int) {
                        adapter.setupViews()
                        adapter.playButton.setOnClickListener {
                            play(song[position].uri)
                            adapter.showPauseButton()
                        }
                        adapter.pauseButton.setOnClickListener {
                            pause()
                            adapter.showResumeButton()
                        }
                        adapter.resumeButton.setOnClickListener {
                            resume()
                            adapter.showPauseButton()
                        }
                    }
                })
            }
        })
    }

    fun play(uri: String) {
        spotifyAppRemote?.playerApi?.play(uri)
    }

    fun resume() {
        spotifyAppRemote?.playerApi?.resume()
    }

    fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }

    fun playingState(handler: (PlayingState) -> Unit) {
        spotifyAppRemote?.playerApi?.playerState?.setResultCallback { result ->
            if (result.track.uri == null) {
                handler(PlayingState.STOPPED)
            } else if (result.isPaused) {
                handler(PlayingState.PAUSED)
            } else {
                handler(PlayingState.PLAYING)
            }
        }
    }

    private fun toolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Songs"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}