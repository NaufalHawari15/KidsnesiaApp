package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ProductResponse(

	@field:SerializedName("listMerchandise")
	val listMerchandise: List<ListMerchandiseItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ListMerchandiseItem(

	@field:SerializedName("deskripsiMerchandise")
	val deskripsiMerchandise: String? = null,

	@field:SerializedName("idMerchandise")
	val idMerchandise: Int? = null,

	@field:SerializedName("fotoMerchandise")
	val fotoMerchandise: String? = null,

	@field:SerializedName("namaMerchandise")
	val namaMerchandise: String? = null,

	@field:SerializedName("stok")
	val stok: Int? = null,

	@field:SerializedName("hargaMerchandise")
	val hargaMerchandise: Int? = null
)
