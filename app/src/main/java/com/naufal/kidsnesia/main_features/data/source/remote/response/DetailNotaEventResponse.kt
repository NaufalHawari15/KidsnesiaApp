package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailNotaEventResponse(

	@field:SerializedName("notaPembelianEvent")
	val notaPembelianEvent: NotaPembelianEvent? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class NotaPembelianEvent(

	@field:SerializedName("tanggalPembelianEvent")
	val tanggalPembelianEvent: String? = null,

	@field:SerializedName("idPembelianEvent")
	val idPembelianEvent: Int? = null,

	@field:SerializedName("emailPelanggan")
	val emailPelanggan: String? = null,

	@field:SerializedName("totalPembelianEvent")
	val totalPembelianEvent: Int? = null,

	@field:SerializedName("idPembayaranEvent")
	val idPembayaranEvent: Int? = null,

	@field:SerializedName("namaPelanggan")
	val namaPelanggan: String? = null,

	@field:SerializedName("teleponPelanggan")
	val teleponPelanggan: String? = null,

	@field:SerializedName("detailEvent")
	val detailEvent: List<DetailNotaEventItem?>? = null,

	@field:SerializedName("statusPembayaranEvent")
	val statusPembayaranEvent: String? = null,

	@field:SerializedName("statusPembelianEvent")
	val statusPembelianEvent: String? = null
)

data class DetailNotaEventItem(

	@field:SerializedName("hargaEvent")
	val hargaEvent: Int? = null,

	@field:SerializedName("idDetailPembelianEvent")
	val idDetailPembelianEvent: Int? = null,

	@field:SerializedName("idEvent")
	val idEvent: Int? = null,

	@field:SerializedName("namaEvent")
	val namaEvent: String? = null,

	@field:SerializedName("subtotalEvent")
	val subtotalEvent: Int? = null,

	@field:SerializedName("tanggalEvent")
	val tanggalEvent: String? = null,

	@field:SerializedName("jadwalEvent")
	val jadwalEvent: String? = null,

	@field:SerializedName("jumlahTiket")
	val jumlahTiket: Int? = null
)
