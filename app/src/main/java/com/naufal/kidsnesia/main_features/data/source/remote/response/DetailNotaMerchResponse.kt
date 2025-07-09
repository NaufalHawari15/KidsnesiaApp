package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailNotaMerchResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("notaPembelianMerch")
	val notaPembelianMerch: NotaPembelianMerch? = null
)

data class DetailMerchItem(

	@field:SerializedName("hargaMerch")
	val hargaMerch: Int? = null,

	@field:SerializedName("subtotalMerch")
	val subtotalMerch: Int? = null,

	@field:SerializedName("jumlahMerch")
	val jumlahMerch: Int? = null,

	@field:SerializedName("idDetailPembelianMerch")
	val idDetailPembelianMerch: Int? = null,

	@field:SerializedName("idMerch")
	val idMerch: Int? = null,

	@field:SerializedName("namaMerch")
	val namaMerch: String? = null
)

data class NotaPembelianMerch(

	@field:SerializedName("idPembayaranMerch")
	val idPembayaranMerch: Int? = null,

	@field:SerializedName("emailPelanggan")
	val emailPelanggan: String? = null,

	@field:SerializedName("detailMerch")
	val detailMerch: List<DetailMerchItem?>? = null,

	@field:SerializedName("namaPelanggan")
	val namaPelanggan: String? = null,

	@field:SerializedName("statusPembayaranMerch")
	val statusPembayaranMerch: String? = null,

	@field:SerializedName("teleponPelanggan")
	val teleponPelanggan: String? = null,

	@field:SerializedName("totalPembelianMerch")
	val totalPembelianMerch: Int? = null,

	@field:SerializedName("statusPembelianMerch")
	val statusPembelianMerch: String? = null,

	@field:SerializedName("idPembelianMerch")
	val idPembelianMerch: Int? = null,

	@field:SerializedName("tanggalPembelianMerch")
	val tanggalPembelianMerch: String? = null
)
