package com.example.musicapplication_sp.activities

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.musicapplication_sp.PlaylistAdapter
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.interfaces.VolleyCallBack
import com.example.musicapplication_sp.model.PlaylistModel
import com.example.musicapplication_sp.repositories.PlaylistService
import com.example.musicapplication_sp.repositories.UserService
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

//PlaylistUpdateDelete
class ActivityPlaylist : AppCompatActivity(){
    private lateinit var btnFab: FloatingActionButton
    private lateinit var database: FirebaseFirestore
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var rQueue: RequestQueue
    private lateinit var playlistService: PlaylistService
    private lateinit var userService: UserService
    private lateinit var editor: SharedPreferences.Editor
    var lists: MutableList<PlaylistModel>? = null
    private lateinit var playlistAdapter: PlaylistAdapter
    private var listViewItem: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)

        database = FirebaseFirestore.getInstance()
        btnFab = findViewById(R.id.btnFab)
        listViewItem = findViewById(R.id.itemPlaylist)
        sharedPreferences = this.getSharedPreferences("Spotify", MODE_PRIVATE);
        rQueue = Volley.newRequestQueue(this)
        playlistService = PlaylistService(rQueue, sharedPreferences)
        userService = UserService(rQueue, sharedPreferences)
        addNewPlaylist()
    }

    /**
     * addNewPlaylist() opens a alert dialog box for adding new playlist
     */
    private fun addNewPlaylist() {
        btnFab.setOnClickListener { view ->
            //start dialog box
            val alertDialog = AlertDialog.Builder(this)
            val textEditText = EditText(this)
            alertDialog.setTitle("Enter New Playlist")
            alertDialog.setMessage("Add Playlist")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Create playlist") { dialog, i ->

                val playlistItemData = PlaylistModel.createList()
                playlistItemData.itemDatatext = textEditText.text.toString()

                userService.get(object : VolleyCallBack {
                    override fun onSuccess() {
                        val user = userService.getUser()
                        playlistService.createPlaylist(user.id, playlistItemData.itemDatatext)
                    }
                })
                playlistItemData.delete = false

                dialog.dismiss()
                Toast.makeText(this, "Playlist saved", Toast.LENGTH_LONG).show()
            }
            alertDialog.show()
            //End dialog box with creation of list and db connection(Realtime database)
        }

//        lists = mutableListOf()
//        playlistAdapter = PlaylistAdapter(this, lists!!)
//        listViewItem!!.adapter=playlistAdapter
//        database.addValueEventListener(object : ValueEventListener{
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                lists!!.clear()
//                addItemToList(snapshot)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "No item Added",Toast.LENGTH_LONG).show()
//            }
//        })
    }
//    private fun addItemToList(snapshot: DataSnapshot){
//        val items = snapshot.children.iterator()
//
//        if(items.hasNext()){
//            val playlistIndexedValue = items.next()
//            val itemsIterator = playlistIndexedValue.children.iterator()
//
//            while (itemsIterator.hasNext()){
//                val currentItem = itemsIterator.next()
//                val playListItemData = PlaylistModel.createList()
//                val map = currentItem.getValue() as HashMap<String, Any>
//
//                playListItemData.UID = currentItem.key
//                playListItemData.delete = map.get("Deleted") as Boolean?
//                playListItemData.itemDatatext = map.get("itemTextData") as String?
//                lists!!.add(playListItemData)
//            }
//        }
//        playlistAdapter.notifyDataSetChanged()
//    }
//
//    override fun modifyItem(itemUID: String, isDone: Boolean) {
//        val playlistReference = database.child("Playlist").child(itemUID)
//        playlistReference.child("done").setValue(isDone)
//    }
//
//    override fun onItemDelete(itemUID: String) {
//        val playlistReference = database.child("Playlist").child(itemUID)
//        playlistReference.removeValue()
//        playlistAdapter.notifyDataSetChanged()
//    }
}