package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PilihBankMerchResponse(

	@field:SerializedName("dataPembayaranMerch")
	val dataPembayaranMerch: DataPembayaranMerch? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataPembayaranMerch(

	@field:SerializedName("idPembayaranMerch")
	val idPembayaranMerch: Int? = null,

	@field:SerializedName("bankPengirim")
	val bankPengirim: String? = null,

	@field:SerializedName("totalHargaMerch")
	val totalHargaMerch: Int? = null,

	@field:SerializedName("statusPembayaranMerch")
	val statusPembayaranMerch: String? = null,

	@field:SerializedName("idPembelianMerch")
	val idPembelianMerch: Int? = null
)
