package com.example.musicapplication_sp.adaptermodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.interfaces.OnSongClickListener
import com.example.musicapplication_sp.model.Song

class SongAdapter(private val songs: ArrayList<Song>): RecyclerView.Adapter<SongAdapter.MyViewHolder>() {
    private lateinit var listener: OnSongClickListener
    lateinit var playButton: ImageButton
    lateinit var pauseButton: ImageButton
    lateinit var resumeButton: ImageButton
    fun setOnSongPlayListener(listener: OnSongClickListener) {
        this.listener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item_songs, parent, false)
        playButton = itemView.findViewById(R.id.playButton)
        pauseButton = itemView.findViewById(R.id.pauseButton)
        resumeButton = itemView.findViewById(R.id.resumeButton)
        return MyViewHolder(itemView, this.listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = songs[position]
        holder.songName.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return songs.size

    }
    inner class MyViewHolder(itemView: View, listener: OnSongClickListener): RecyclerView.ViewHolder(itemView) {
        val songName : TextView = itemView.findViewById(R.id.song_name)
        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
         }
        }
}