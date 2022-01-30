package com.example.musicapplication_sp.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.PostAdapter
import com.example.musicapplication_sp.interfaces.SonglistCrudMethod
import com.example.musicapplication_sp.model.GetSongsModel
import com.example.musicapplication_sp.data.SongResponse
import com.example.musicapplication_sp.repositories.SongListService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SonglistActivity : AppCompatActivity() {

    private lateinit var songInput: EditText
    private lateinit var addButton: Button

    private lateinit var listOfSongs: RecyclerView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        songInput = findViewById(R.id.song_input)
        addButton = findViewById(R.id.add_song_button)
        listOfSongs = findViewById(R.id.list_of_songs)

        listOfSongs.layoutManager = LinearLayoutManager(this)
        listOfSongs.setHasFixedSize(true)
        getListOfSongs { songs: List<GetSongsModel> ->
            listOfSongs.adapter = PostAdapter(songs)
        }

        postListOfSongs {  }
    }

    /**
     * Retrieve Data from API which is stored in MySql database
     */
    private fun getListOfSongs(callback: (List<GetSongsModel>) -> Unit) {
        val api = SongListService.getInstance().create(SonglistCrudMethod::class.java)
        api.getSongs().enqueue(object : Callback<SongResponse> {
            override fun onResponse(call: Call<SongResponse>, response: Response<SongResponse>) {
                return callback(response.body()!!.songs)
            }

            override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                t.printStackTrace()
                Log.d("SonglistActivity", "onFailure: " + t.message.toString())
            }
        })
    }

    /**
     * Insert Data to API to store. DOES NOT WORK YET!!
     */
    private fun postListOfSongs(callback: (List<GetSongsModel>) -> Unit) {
        val api = SongListService.getInstance().create(SonglistCrudMethod::class.java)

        addButton.setOnClickListener {
            api.postSongs().enqueue(object : Callback<SongResponse> {
                override fun onResponse(
                    call: Call<SongResponse>,
                    response: Response<SongResponse>
                ) {
                    return callback(response.body()!!.songs)
                }

                override fun onFailure(call: Call<SongResponse>, t: Throwable) {
                    t.printStackTrace()
                    Log.d("SonglistActivity", "onFailure: " + t.message.toString())
                }
            })
        }
    }
}