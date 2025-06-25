package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListCartResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("listEventCart")
	val listEventCart: List<ListEventCartItem?>? = null
)

data class ListEventCartItem(

	@field:SerializedName("tanggalPembelianEvent")
	val tanggalPembelianEvent: Any? = null,

	@field:SerializedName("idPembelianEvent")
	val idPembelianEvent: Int? = null,

	@field:SerializedName("totalPembelianEvent")
	val totalPembelianEvent: Int? = null,

	@field:SerializedName("cartEventItem")
	val cartEventItem: List<CartEventItemItems?>? = null,

	@field:SerializedName("statusPembelianEvent")
	val statusPembelianEvent: String? = null
)

data class CartEventItemItems(

	@field:SerializedName("fotoEvent")
	val fotoEvent: String? = null,

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
