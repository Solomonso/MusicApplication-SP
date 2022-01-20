package com.example.musicapplication_sp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.PlaylistAdapter
import com.example.musicapplication_sp.interfaces.OnPlaylistClickListener
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.Playlist
import com.example.musicapplication_sp.model.PlaylistModel
import com.example.musicapplication_sp.repositories.PlaylistService
import com.example.musicapplication_sp.repositories.UserService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

//PlaylistUpdateDelete
class PlaylistActivity : AppCompatActivity() {
    private lateinit var btnFab: FloatingActionButton
    private lateinit var database: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rQueue: RequestQueue
    private lateinit var playlistService: PlaylistService
    private lateinit var userService: UserService
    private lateinit var recyclerView: RecyclerView
    private lateinit var playlists: ArrayList<Playlist>

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        database = FirebaseFirestore.getInstance()
        btnFab = findViewById(R.id.btnFab)
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE)
        rQueue = Volley.newRequestQueue(this)
        playlistService = PlaylistService(rQueue, sharedPreferences)
        userService = UserService(rQueue, sharedPreferences)
        addNewPlaylist()
        recyclerView = findViewById(R.id.itemPlaylist)
        recyclerView.layoutManager = LinearLayoutManager(this)
        playlists = arrayListOf()
        retrieveUserPlaylist()

    }

    private fun retrieveUserPlaylist() {
        playlistService.getCurrentUserPlaylist(object : VolleyCallBack {
            override fun onSuccess() {
                val playlist = playlistService.playlist
                for (p in playlist) {
                    playlists.add(p)
                }
                val adapter = PlaylistAdapter(playlists)

                recyclerView.adapter = adapter
                adapter.setOnPlaylistClickListener(object : OnPlaylistClickListener {
                    override fun onItemClick(position: Int) {
                        val songIntent = Intent(this@PlaylistActivity, SongActivity::class.java)
                        songIntent.putExtra("playlist_id", playlists[position].id)
                        startActivity(songIntent)

                    }
                })

            }
        })
    }

    /**
     * addNewPlaylist() opens a alert dialog box for adding new playlist
     */
    private fun addNewPlaylist() {
        btnFab.setOnClickListener {
            //start dialog box
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setTitle("Enter New Playlist")
            alertDialog.setMessage("Add Playlist")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Create playlist") { dialog, _ ->

                val playlistItemData = PlaylistModel.createList()
                playlistItemData.itemDatatext = textEditText.text.toString()
                userService.get(object : VolleyCallBack {
                    override fun onSuccess() {
                        val user = userService.getUser()
                        playlistService.createPlaylist(user.id, playlistItemData.itemDatatext)
                    }
                })
                dialog.dismiss()
                Toast.makeText(this, "Playlist saved", Toast.LENGTH_LONG).show()
            }
            alertDialog.show()
        }

    }
}