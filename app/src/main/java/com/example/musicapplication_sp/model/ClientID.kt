package com.example.musicapplication_sp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClientID(
    @SerializedName("ClientID")
    val ClientID: String?,
    @SerializedName("iv")
    val iv: String?
) : Parcelable {
    constructor() : this("", "")
}
