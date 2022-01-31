package com.example.musicapplication_sp.adaptermodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapplication_sp.R
import com.example.musicapplication_sp.model.GetSongsModel

class PostAdapter(private val getSongsModel: List<GetSongsModel>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var tvId: TextView = itemView.findViewById(R.id.tvId)
        private var tvUserId: TextView = itemView.findViewById(R.id.tvUserId)
        private var tvSongName: TextView = itemView.findViewById(R.id.tvSongName)
        /**
         * bindView method takes one GetSongsModel object and loads its content in the list item.
         */
        fun bindView(getSongsModel: GetSongsModel) {
            tvId.text = getSongsModel.id.toString()
            tvUserId.text = getSongsModel.UserID.toString()
            tvSongName.text = getSongsModel.song_name.toString()
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
