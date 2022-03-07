package com.example.musicapplication_sp.adaptermodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.interfaces.OnPlaylistClickListener
import com.example.musicapplication_sp.interfaces.OnYoutubePlaylistClickListener
import com.example.musicapplication_sp.model.YtPlaylist

class YoutubePlaylistAdapter(private val playlists: ArrayList<YtPlaylist>) :
    RecyclerView.Adapter<YoutubePlaylistAdapter.MyViewHolder>() {
    private lateinit var listener: OnYoutubePlaylistClickListener

    fun setOnPlaylistClickListener(listener: OnYoutubePlaylistClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.yt_playlists, parent, false)
        return MyViewHolder(itemView, this.listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = playlists[position]
        holder.playlistName.text = currentItem.snippet?.title

    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    inner class MyViewHolder(itemView: View, listener: OnYoutubePlaylistClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val playlistName: TextView = itemView.findViewById(R.id.playlist_name)

        init {
            itemView.setOnClickListener {
                val currentItem = playlists[layoutPosition]
                listener.onItemClick(currentItem.id)
            }
        }

    }
}