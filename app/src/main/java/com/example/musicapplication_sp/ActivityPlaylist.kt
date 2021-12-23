package com.example.musicapplication_sp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore




class ActivityPlaylist : AppCompatActivity()
{
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        val database = FirebaseFirestore.getInstance()

        addNewPlaylist()
    }

    /**
     * addnewPlaylist() creates window box for adding new playlist
     */
    private fun addNewPlaylist() {
        val fab = findViewById<View>(R.id.btnFab) as FloatingActionButton
        fab.setOnClickListener { view ->
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setTitle("Enter New Playlist")
            alertDialog.setMessage("Add Playlist")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Create playlist") { dialog, i ->
                val playlistItemData = PlaylistModel.createList()
                playlistItemData.itemDatatext = textEditText.text.toString()
                playlistItemData.delete = false

//                val newItemData = database.collection("Playlist")

            }
            alertDialog.show()
        }
    }
}