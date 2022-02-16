package com.example.musicapplication_sp.activities

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.PostAdapter
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

    init{
        System.loadLibrary("keys")
    }
    private lateinit var songInput: EditText
    private lateinit var addButton: Button
    private lateinit var listOfSongs: RecyclerView
    private lateinit var auth: FirebaseAuth
    private external fun getTokenKey(): String
    var token : String = getTokenKey()

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        songInput = findViewById(R.id.song_input)
        addButton = findViewById(R.id.add_song_button)
        listOfSongs = findViewById(R.id.list_of_songs)
        auth = Firebase.auth
        listOfSongs.layoutManager = LinearLayoutManager(this)
        listOfSongs.setHasFixedSize(true)

        this.getListOfSongs { songs: List<GetSongsModel> ->
            listOfSongs.adapter = PostAdapter(songs)
        }

        addButton.setOnClickListener {
            this.insertSong()
            Toast.makeText(
                applicationContext, "Song Inserted", Toast.LENGTH_SHORT
            ).show()
//            true
        }
    }

//    var client: APIService = retrofit.create(APIService::class.java)
//
//    var calltargetResponse: Call<UserProfile> = client.getUser("0034", "Bearer $token")
//    https://stackoverflow.com/questions/41078866/retrofit2-authorization-global-interceptor-for-access-token#41082979
    /**
     * Retrieve Data from API which is stored in MySql database
     */
    private fun getListOfSongs(callback: (List<GetSongsModel>) -> Unit) {
        val api = SongListService.getInstance().create(SonglistCrudMethod::class.java)
        //val userId = auth.currentUser!!.uid
        val callSong: Call<SongResponse> = api.getSongs(token)

        callSong.enqueue(object : Callback<SongResponse> {
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
     * insertSong() method posts an submitted songs to api
     */
    private fun insertSong() {
        try {
            val song: String = songInput.text.toString().trim { it <= ' ' }
            val id = auth.currentUser!!.uid

            val postSongsModel = PostSongsModel(
                UserID = id,
                songName = song
            )
            postListOfSongs(postSongsModel) {
                if (it.UserID != null) {
                    return@postListOfSongs
                } else {
                    Log.d("SonglistActivity", "onFailure: " + toString())
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                this@SonglistActivity,
                e.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * postListOfSongs()
     */
    private fun postListOfSongs(params: PostSongsModel, onResult: (PostSongsModel) -> Unit) {
        val api = SongListService.getInstance().create(SonglistCrudMethod::class.java)
        api.postSongs(params).enqueue(object : Callback<PostSongsModel> {
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
}