package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailCartResponse(

	@field:SerializedName("cartEventDetail")
	val cartEventDetail: CartEventDetail? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class CartEventDetail(

	@field:SerializedName("tanggalPembelianEvent")
	val tanggalPembelianEvent: Any? = null,

	@field:SerializedName("idPembelianEvent")
	val idPembelianEvent: Int? = null,

	@field:SerializedName("cartEventItem")
	val cartEventItem: List<CartEventItem?>? = null,

	@field:SerializedName("totalHargaEvent")
	val totalHargaEvent: Int? = null,

	@field:SerializedName("statusPembelianEvent")
	val statusPembelianEvent: String? = null
)

data class CartEventItem(

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

	@field:SerializedName("jadwalEvent")
	val jadwalEvent: String? = null,

	@field:SerializedName("jumlahTiket")
	val jumlahTiket: Int? = null
)
