package com.naufal.kidsnesia.auth.domain.model

data class User(
    val namaPelanggan: String = "",
    val email: String = "",
    val password: String = "",
    val noHpPelanggan: String = ""
)
