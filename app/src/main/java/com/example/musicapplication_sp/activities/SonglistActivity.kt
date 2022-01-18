package com.example.musicapplication_sp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R

class SonglistActivity : AppCompatActivity() {

    lateinit var songInput : EditText
    lateinit var addButton : Button
    lateinit var listViewer : RecyclerView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)
        songInput = findViewById(R.id.song_input)
        addButton = findViewById(R.id.add_song_button)
        addButton.setOnClickListener{
            addSong()
        }
        listViewer = findViewById(R.id.list_of_songs)
    }

    fun addSong() {
        val songName = songInput.text.toString()
        val songList = mutableListOf<String>()

        for(song in listViewer){
            songList.add(song.toString())
        }

        if(songName.isNotEmpty() && songName.isNotBlank()){
            TODO("Add song to the recyclerview")

        }
    }

    override fun onStart() {
        super.onStart()
    }


    override fun onStop() {
        super.onStop()
    }

}