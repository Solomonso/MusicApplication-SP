package com.example.musicapplication_sp.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.interfaces.SonglistApiService
import com.example.musicapplication_sp.model.PostModel
import com.example.musicapplication_sp.repositories.SonglistServiceGenerator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SonglistActivity : AppCompatActivity() {

    private lateinit var songInput: EditText
    private lateinit var addButton: Button
    private lateinit var listViewer: RecyclerView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        val otherSongService = SonglistServiceGenerator.buildService(SonglistApiService::class.java)
        val call = otherSongService.getPosts()

        songInput = findViewById(R.id.song_input)
        addButton = findViewById(R.id.add_song_button)
        listViewer = findViewById(R.id.list_of_songs)

        addButton.setOnClickListener {
            //addSong()
            call.enqueue(object : Callback<MutableList<PostModel>> {
                override fun onResponse(
                    call: Call<MutableList<PostModel>>,
                    response: Response<MutableList<PostModel>>
                ) {
                    if (response.isSuccessful) {
                        Log.e("SUCCESS", response.body().toString())
                    }
                }

                override fun onFailure(call: Call<MutableList<PostModel>>, t: Throwable) {
                    t.printStackTrace()
                    Log.e("ERROR", t.message.toString())
                }

            })
        }

    }

    fun addSong() {
        val songName = songInput.text.toString()
        val songList = mutableListOf<String>()

        for (song in listViewer) {
            songList.add(song.toString())
        }

        if (songName.isNotEmpty() && songName.isNotBlank()) {
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