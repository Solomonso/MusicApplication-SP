package com.example.musicapplication_sp.activities

import android.media.MediaRouter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.GetAdapter
import com.example.musicapplication_sp.adaptermodel.SongAdapter
import com.example.musicapplication_sp.interfaces.OnSongClickListener
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.UserSongsModel
import com.example.musicapplication_sp.repositories.StoredUserSongsService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UsersSongsActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    private lateinit var songs: ArrayList<UserSongsModel>
    private lateinit var userSongService: StoredUserSongsService
    private lateinit var userID: String
    private lateinit var songInput: EditText
    private lateinit var addButton: Button
    private lateinit var listOfSongs: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)
        requestQueue = Volley.newRequestQueue(this)
        userSongService = StoredUserSongsService(requestQueue)
        auth = Firebase.auth
//        songInput = findViewById(R.id.song_input)
//        addButton = findViewById(R.id.add_song_button)

        userID = intent.getStringExtra("UserId").toString()
        listOfSongs = findViewById(R.id.list_of_songs)
        listOfSongs.layoutManager = LinearLayoutManager(this)
        songs = arrayListOf()

        retrieveSongFromPlaylist()
    }

    private fun retrieveSongFromPlaylist() {
        val userId = "WgmyK3I1DBbnO1TVf9z9EKpTqRd2"
        userSongService.getCurrentUserPlaylist(userId, object : VolleyCallBack {
            override fun onSuccess() {
                val song = userSongService.listOfSongs
                for (s in song) {
                    songs.add(s)
                    Log.d("track info ", s.songName)
                }
                val adapter = GetAdapter(songs)

                listOfSongs.adapter = GetAdapter(songs)
            }
        })
    }
}