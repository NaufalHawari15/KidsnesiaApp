package com.naufal.kidsnesia.auth.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nama_pelanggan")
    val namaPelanggan: String,

    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("no_hp_pelanggan")
    val noHpPelanggan: String
)
