package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class NotaResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("nota")
	val nota: Nota? = null
)

data class DetailEventItem(

	@field:SerializedName("hargaEvent")
	val hargaEvent: Int? = null,

	@field:SerializedName("namaEvent")
	val namaEvent: String? = null,

	@field:SerializedName("jadwalEvent")
	val jadwalEvent: String? = null,

	@field:SerializedName("jumlahTiket")
	val jumlahTiket: Int? = null
)

data class Nota(

	@field:SerializedName("idPembelian")
	val idPembelian: Int? = null,

	@field:SerializedName("tanggalBayar")
	val tanggalBayar: String? = null,

	@field:SerializedName("bank")
	val bank: String? = null,

	@field:SerializedName("tanggalPembelian")
	val tanggalPembelian: String? = null,

	@field:SerializedName("namaPelanggan")
	val namaPelanggan: String? = null,

	@field:SerializedName("totalHarga")
	val totalHarga: Int? = null,

	@field:SerializedName("detailEvent")
	val detailEvent: List<DetailEventItem?>? = null,

	@field:SerializedName("idPembayaran")
	val idPembayaran: Int? = null,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("noHpPelanggan")
	val noHpPelanggan: String? = null
)
