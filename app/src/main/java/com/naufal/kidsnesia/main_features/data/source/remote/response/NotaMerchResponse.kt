package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class NotaMerchResponse(

	@field:SerializedName("listNotaPembelianMerch")
	val listNotaPembelianMerch: List<ListNotaPembelianMerchItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class ListNotaPembelianMerchItem(

	@field:SerializedName("statusPembayaranMerch")
	val statusPembayaranMerch: String? = null,

	@field:SerializedName("totalPembelianMerch")
	val totalPembelianMerch: Int? = null,

	@field:SerializedName("statusPembelianMerch")
	val statusPembelianMerch: String? = null,

	@field:SerializedName("idPembelianMerch")
	val idPembelianMerch: Int? = null,

	@field:SerializedName("tanggalPembelianMerch")
	val tanggalPembelianMerch: String? = null
)
