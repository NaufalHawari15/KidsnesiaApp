package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CartResponse(

	@field:SerializedName("pembelianEventResponse")
	val pembelianEventResponse: PembelianEventResponse? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class PembelianEventResponse(

	@field:SerializedName("tanggalPembelianEvent")
	val tanggalPembelianEvent: Any? = null,

	@field:SerializedName("idPembelianEvent")
	val idPembelianEvent: Int? = null,

	@field:SerializedName("cartEventItem")
	val cartEventItem: List<CartEventItemItem?>? = null,

	@field:SerializedName("totalHargaEvent")
	val totalHargaEvent: Int? = null,

	@field:SerializedName("statusPembelianEvent")
	val statusPembelianEvent: String? = null
)

data class CartEventItemItem(

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

	@field:SerializedName("jumlahTiket")
	val jumlahTiket: Int? = null
)
