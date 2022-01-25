package com.example.musicapplication_sp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongResponse(
    @SerializedName("data")
    val songs: List<PostModel>
) : Parcelable {
    constructor() : this(mutableListOf())
}