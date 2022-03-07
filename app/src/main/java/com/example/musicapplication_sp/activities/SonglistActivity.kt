package com.example.musicapplication_sp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.GetAdapter
import com.example.musicapplication_sp.data.SongResponse
import com.example.musicapplication_sp.interfaces.SonglistCrudMethod
import com.example.musicapplication_sp.model.GetSongsModel
import com.example.musicapplication_sp.model.PostSongsModel
import com.example.musicapplication_sp.repositories.SongListService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SonglistActivity : AppCompatActivity() {

    init {
        System.loadLibrary("keys")
    }

    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var songInput: EditText
    private lateinit var addButton: Button
    private lateinit var listOfSongs: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var toolbar: Toolbar
    private external fun getKey(): String
    var token: String = getKey()

    @SuppressLint("NotifyDataSetChanged")
    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        songInput = findViewById(R.id.song_input)
        addButton = findViewById(R.id.add_song_button)
        listOfSongs = findViewById(R.id.list_of_songs)
        auth = Firebase.auth
        listOfSongs.layoutManager = LinearLayoutManager(this)
        swipe = findViewById(R.id.swipeSongs)

        toolbar = findViewById(R.id.toolbar)
        this.toolbar()

        swipe.setOnRefreshListener {
            if (swipe.isRefreshing) {
                swipe.isRefreshing = false
            }
        }

        this.getListOfSongs { songs: List<GetSongsModel> ->
            listOfSongs.adapter = GetAdapter(songs)
        }

        this.insertSong()
    }

    /**
     * @getListOfSongs() Retrieve Data from API which is stored in MySql database
     */
    private fun getListOfSongs(callback: (List<GetSongsModel>) -> Unit) {
        val userId = auth.currentUser!!.uid.trim()
        val api = SongListService.getInstance().create(SonglistCrudMethod::class.java)

        api.getSongsById(userId, token).enqueue(object : Callback<SongResponse> {
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
     * @insertSong() method posts an submitted songs to api
     */
    private fun insertSong() {
        addButton.setOnClickListener {
            try {
                val song: String = songInput.text.toString().trim()
                val id = auth.currentUser!!.uid.trim()

                if (song.isEmpty()){
                    Toast.makeText(this@SonglistActivity, "Please enter the song", Toast.LENGTH_SHORT).show()
                } else {
                    val postSongsModel = PostSongsModel(
                        UserID = id,
                        songName = song
                    )
                    postListOfSongs(postSongsModel) {
                        if (it.UserID != null || it.songName != null) {
                            return@postListOfSongs
                        } else {
                            Log.d("SonglistActivity", "onFailure: " + toString())
                        }
                    }
                    Toast.makeText(this@SonglistActivity, "Song added", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@SonglistActivity,
                    e.message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    /**
     * @postListOfSongs()
     */
    private fun postListOfSongs(params: PostSongsModel, onResult: (PostSongsModel) -> Unit) {
        val api = SongListService.getInstance().create(SonglistCrudMethod::class.java)
        api.postSongs(params, token).enqueue(object : Callback<PostSongsModel> {
            override fun onResponse(
                call: Call<PostSongsModel>,
                response: Response<PostSongsModel>
            ) {
                val addedSong = response.body()
                if (addedSong != null) {
                    onResult(addedSong)
                }
            }

            override fun onFailure(call: Call<PostSongsModel>, t: Throwable) {
                t.printStackTrace()
                Log.d("SonglistActivity", "onFailure: " + t.message.toString())
            }
        })
    }

    /**
     * @toolbar returns the actionbar elements on toolbar.xml
     */
    private fun toolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Settings"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}