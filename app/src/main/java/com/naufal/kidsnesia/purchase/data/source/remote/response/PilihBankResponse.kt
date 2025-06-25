package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PilihBankResponse(

	@field:SerializedName("dataPembayaranEvent")
	val dataPembayaranEvent: DataPembayaranEvent? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataPembayaranEvent(

	@field:SerializedName("bankPengirim")
	val bankPengirim: String? = null,

	@field:SerializedName("idPembelianEvent")
	val idPembelianEvent: Int? = null,

	@field:SerializedName("idPembayaranEvent")
	val idPembayaranEvent: Int? = null,

	@field:SerializedName("statusPembayaranEvent")
	val statusPembayaranEvent: String? = null,

	@field:SerializedName("totalHargaEvent")
	val totalHargaEvent: Int? = null
)
