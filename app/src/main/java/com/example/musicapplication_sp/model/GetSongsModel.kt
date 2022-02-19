package com.example.musicapplication_sp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetSongsModel(
//    @SerializedName("UserID")
//    val UserID: String?,
    @SerializedName("songName")
    val songName: String?
) : Parcelable {
    constructor() : this("")
}