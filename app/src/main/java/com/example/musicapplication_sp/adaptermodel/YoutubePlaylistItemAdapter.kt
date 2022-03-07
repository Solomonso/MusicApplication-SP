package com.example.musicapplication_sp.adaptermodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.interfaces.OnYoutubePlaylistItemClickListener
import com.example.musicapplication_sp.model.YtPlaylistItem

class YoutubePlaylistItemAdapter(private val playlists: ArrayList<YtPlaylistItem>) :
    RecyclerView.Adapter<YoutubePlaylistItemAdapter.MyViewHolder>() {
    private lateinit var listener: OnYoutubePlaylistItemClickListener

    fun setOnPlaylistClickListener(listener: OnYoutubePlaylistItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.yt_playlistitems, parent, false)
        return MyViewHolder(itemView, this.listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = playlists[position]
        holder.playlistName.text = currentItem.snippet?.title

    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    inner class MyViewHolder(itemView: View, listener: OnYoutubePlaylistItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val playlistName: TextView = itemView.findViewById(R.id.item_name)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(playlists[layoutPosition].resourceId?.videoId)
            }
        }

    }
}