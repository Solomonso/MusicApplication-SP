package com.example.musicapplication_sp.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.adaptermodel.PlaylistAdapter
import com.example.musicapplication_sp.cryptography.Cryptography
import com.example.musicapplication_sp.interfaces.OnPlaylistClickListener
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.Endpoints
import com.example.musicapplication_sp.model.Playlist
import com.example.musicapplication_sp.model.PlaylistModel
import com.example.musicapplication_sp.repositories.PlaylistService
import com.example.musicapplication_sp.repositories.UserService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.util.*

//PlaylistUpdateDelete
class PlaylistActivity : AppCompatActivity() {
    init{
        System.loadLibrary("keys")
    }
    private lateinit var btnFab: FloatingActionButton
    private lateinit var database: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rQueue: RequestQueue
    private lateinit var playlistService: PlaylistService
    private lateinit var userService: UserService
    private lateinit var recyclerView: RecyclerView
    private lateinit var toolbar: Toolbar
    private lateinit var playlists: ArrayList<Playlist>
    private external fun getTokenKey(): String
    var token : String = getTokenKey()

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
        getTheClientID("6hh8Uyhy37XnnKIAxLQjMRIYZn736hh8Uyhy37XnnKIAxLQjMRIYZn73")

        toolbar = findViewById(R.id.toolbarPlaylist)
        this.toolbar()
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

    private fun toolbar() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Playlist"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun getTheClientID(UserID: String?) {
        val endpoint = String.format(
            Endpoints.GETCLIENTID.endpoint,
            UserID
        ) //format the url to get the playlist id
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, endpoint, null,
            Response.Listener { response: JSONObject ->
                val jsonArray = response.optJSONArray("data")
                for (i in 0 until Objects.requireNonNull(jsonArray).length()) {
                    try {
                        val jsonObject = jsonArray?.getJSONObject(i)
                        //     Toast.makeText(this, "id: " + jsonObject.getString("id") + " client id " + jsonObject.getString("ClientID") + " iv " + jsonObject.getString("iv"), Toast.LENGTH_SHORT).show();
//                        Log.d(
//                            "Test ",
//                            " client id " + jsonObject?.getString("ClientID") + " iv " + Arrays.toString(
//                                jsonObject?.getString("iv")
//                                    .toByteArray(StandardCharsets.UTF_8)
//                            )
//                        )
                        val cryptography = Cryptography()
                        val ks: KeyStore = KeyStore.getInstance("AndroidKeyStore").apply {
                            load(null)
                        }
                        val alias = "clientIDKey"
                        val entry = ks.getEntry(alias, null) as? KeyStore.SecretKeyEntry
                        cryptography.createSecretKey("AES")
                        val map = HashMap<String, ByteArray>()
                        map[jsonObject?.getString("iv")!!.toByteArray(StandardCharsets.UTF_8).contentToString()] = jsonObject?.getString("ClientID")!!.toByteArray(StandardCharsets.UTF_8)
                        //val result = entry?.let {cryptography.decrypt(map, it.secretKey)}
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            },
            Response.ErrorListener { error: VolleyError ->
                Log.d(
                    "Error",
                    "Unable get client id $error"
                )
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                val auth = "jwt $token"
                headers["Authorization"] = auth
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        rQueue = Volley.newRequestQueue(this)
        rQueue.add(jsonObjectRequest)
    }


}