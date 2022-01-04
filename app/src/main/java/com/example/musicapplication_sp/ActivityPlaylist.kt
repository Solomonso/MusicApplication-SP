package com.example.musicapplication_sp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.collections.Map as Map

//PlaylistUpdateDelete
class ActivityPlaylist : AppCompatActivity(){
//    private lateinit var btnFab: FloatingActionButton
    private lateinit var database: DocumentReference
    private  lateinit var btnFab: Button
    private  lateinit var txtPlaylist: EditText
//    var lists: MutableList<PlaylistModel>? = null
//    private lateinit var playlistAdapter: PlaylistAdapter
//    private var listViewItem: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnewplaylist)

        database = FirebaseFirestore.getInstance().document("Playlist/genre")
        btnFab = findViewById(R.id.btnFab)
        txtPlaylist = findViewById(R.id.txtPlaylist)

        //addNewPlaylist()
        addNewPlaylistToFirebaseCloud()
        btnFab.setOnClickListener {
            view: View? -> addNewPlaylistToFirebaseCloud()
        }
    }

    private fun addNewPlaylistToFirebaseCloud() {
        var playlist = txtPlaylist.text.toString().trim()
        if (playlist.isEmpty()) {
            Toast.makeText(this, "Field is empty:", Toast.LENGTH_LONG).show()
        } else {
            try {
                val items = HashMap<String, Any>()
                items.put("playlist-1", playlist)
                database.collection(playlist).document("song").set(items).addOnSuccessListener {
                   void: Void? -> Toast.makeText(this, "Successfully uploaded to the database :", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    exception: java.lang.Exception -> Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                }
            } catch (e:Exception){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }







    /**
     * @description addNewPlaylist() opens a alert dialog box for adding new playlist
     */
//    private fun addNewPlaylist() {
//        btnFab.setOnClickListener { view ->
//            //start dialog box
//            val alertDialog = AlertDialog.Builder(this)
//            val textEditText = EditText(this)
//            alertDialog.setTitle("Enter New Playlist")
//            alertDialog.setMessage("Add Playlist")
//            alertDialog.setView(textEditText)
//            alertDialog.setPositiveButton("Create playlist") { dialog, i ->
//
//                val playlistItemData = PlaylistModel.createList()
//                playlistItemData.itemDatatext = textEditText.text.toString()
//                playlistItemData.delete = false
//
//                val newItemData = database.child("Playlist")
//                playlistItemData.UID = newItemData.key
//
//                newItemData.setValue(playlistItemData)
//
//                dialog.dismiss()
//                Toast.makeText(this, "Playlist saved", Toast.LENGTH_LONG).show()
//            }
//            alertDialog.show()
//            //End dialog box with creation of list and db connection(Realtime database)
//        }
//
//        lists = mutableListOf()
//        playlistAdapter = PlaylistAdapter(this, lists!!)
//        listViewItem!!.adapter=playlistAdapter
//        database.addValueEventListener(object : ValueEventListener{
//
//           override fun onDataChange(snapshot: DataSnapshot) {
//                lists!!.clear()
//                addItemToList(snapshot)
//            }
//
//           override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "No item Added",Toast.LENGTH_LONG).show()
//            }
//        })
//    }

    /**
     * @description
     */
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
//    private fun modifyItem(itemUID: String, isDone: Boolean) {
//        val playlistReference = database.child("Playlist").child(itemUID)
//        playlistReference.child("done").setValue(isDone)
//    }

    /**
     * @description
     */
//    private fun onItemDelete(itemUID: String) {
//        val playlistReference = database.child("Playlist").child(itemUID)
//        playlistReference.removeValue()
//        playlistAdapter.notifyDataSetChanged()
//    }
}