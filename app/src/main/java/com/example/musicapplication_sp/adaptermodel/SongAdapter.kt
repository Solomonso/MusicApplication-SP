package com.example.musicapplication_sp.adaptermodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.model.Song

class SongAdapter(private val songs: ArrayList<Song>) :
    RecyclerView.Adapter<SongAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_of_songs, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = songs[position]
        holder.songName.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songName: TextView = itemView.findViewById(R.id.song_name)
    }

}