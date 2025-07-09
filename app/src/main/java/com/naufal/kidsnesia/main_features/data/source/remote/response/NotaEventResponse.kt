package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class NotaEventResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("listNotaPembelianEvent")
	val listNotaPembelianEvent: List<ListNotaPembelianEventItem?>? = null
)

data class ListNotaPembelianEventItem(

	@field:SerializedName("tanggalPembelianEvent")
	val tanggalPembelianEvent: String? = null,

	@field:SerializedName("idPembelianEvent")
	val idPembelianEvent: Int? = null,

	@field:SerializedName("totalPembelianEvent")
	val totalPembelianEvent: Int? = null,

	@field:SerializedName("statusPembayaranEvent")
	val statusPembayaranEvent: String? = null,

	@field:SerializedName("statusPembelianEvent")
	val statusPembelianEvent: String? = null
)
