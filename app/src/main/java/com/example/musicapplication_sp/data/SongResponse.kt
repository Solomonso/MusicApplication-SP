package com.example.musicapplication_sp.data

import android.os.Parcelable
import com.example.musicapplication_sp.model.GetSongsModel
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SongResponse(
    @SerializedName("data")
    val songs: List<GetSongsModel>,
) : Parcelable {
    constructor() : this(mutableListOf())
}