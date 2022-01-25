package com.example.musicapplication_sp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostModel(
    @SerializedName("id")
    val id: Int?,

    @SerializedName("UserID")
    val UserID: String?,

    @SerializedName("song_name")
    val song_name: String?
) : Parcelable {
    constructor() : this(null, "", "")
}