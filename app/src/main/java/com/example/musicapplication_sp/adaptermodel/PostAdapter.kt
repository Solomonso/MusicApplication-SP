package com.example.musicapplication_sp.adaptermodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.model.PostModel

class PostAdapter(private val postModel: MutableList<PostModel>) :
    RecyclerView.Adapter<PostViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        return holder.bindView(postModel[position])
    }

    override fun getItemCount(): Int {
        return postModel.size
    }

}

class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val tvSongName: TextView = itemView.findViewById(R.id.tvSongName)

    fun bindView(postModel: PostModel) {
        tvSongName.text = postModel.song_name
    }
}