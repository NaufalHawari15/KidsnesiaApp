package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PelangganResponse(

	@field:SerializedName("message")
	val message: Message? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Message(

	@field:SerializedName("namaPelanggan")
	val namaPelanggan: String? = null,

	@field:SerializedName("fotoProfil")
	val fotoProfil: String? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("noHpPelanggan")
	val noHpPelanggan: String? = null
)
