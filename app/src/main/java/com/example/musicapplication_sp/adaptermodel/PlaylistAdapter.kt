package com.example.musicapplication_sp.adaptermodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.interfaces.OnPlaylistClickListener
import com.example.musicapplication_sp.model.Playlist

class PlaylistAdapter (private val playlists: ArrayList<Playlist>) : RecyclerView.Adapter<PlaylistAdapter.MyViewHolder>() {
    private lateinit var listener: OnPlaylistClickListener

    fun setOnPlaylistClickListener(listener: OnPlaylistClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_playlists, parent, false)
        return MyViewHolder(itemView, this.listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = playlists[position]
        holder.playlistName.text = currentItem.name

    }

    override fun getItemCount(): Int {
       return playlists.size
    }

    inner class MyViewHolder(itemView: View, listener: OnPlaylistClickListener): RecyclerView.ViewHolder(itemView) {
        val playlistName : TextView = itemView.findViewById(R.id.playlist_name)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }
}