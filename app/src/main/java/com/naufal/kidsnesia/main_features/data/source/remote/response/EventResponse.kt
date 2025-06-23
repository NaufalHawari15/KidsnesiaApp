package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class EventResponse(

	@field:SerializedName("listEvent")
	val listEvent: List<ListEventItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ListEventItem(

	@field:SerializedName("fotoEvent")
	val fotoEvent: String? = null,

	@field:SerializedName("hargaEvent")
	val hargaEvent: Int? = null,

	@field:SerializedName("kuota")
	val kuota: Int? = null,

	@field:SerializedName("idEvent")
	val idEvent: Int? = null,

	@field:SerializedName("namaEvent")
	val namaEvent: String? = null,

	@field:SerializedName("deskripsiEvent")
	val deskripsiEvent: String? = null,

	@field:SerializedName("fotoKegiatan")
	val fotoKegiatan: List<String?>? = null,

	@field:SerializedName("jadwalEvent")
	val jadwalEvent: String? = null
)
