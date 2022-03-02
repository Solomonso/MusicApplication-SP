package com.example.musicapplication_sp.adaptermodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.model.GetSongsModel
import com.example.musicapplication_sp.model.UserSongsModel

class GetAdapter(private val getSongsModel: ArrayList<UserSongsModel>) :
    RecyclerView.Adapter<GetAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var tvSongName: TextView = itemView.findViewById(R.id.tvSongName)
        /**
         * bindView method takes one GetSongsModel object and loads its content in the list item.
         */
        fun bindView(getSongsModel: UserSongsModel) {
            tvSongName.text = getSongsModel.songName.toString()
        }
    }

    /**
     * onCreateViewHolder loads the layout for the list items
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.card_post, parent, false)
        )
    }

    /**
     * getItemCount returns the total number of items to show in the recyclerview
     */
    override fun getItemCount(): Int = getSongsModel.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindView(getSongsModel[position])
    }

}
