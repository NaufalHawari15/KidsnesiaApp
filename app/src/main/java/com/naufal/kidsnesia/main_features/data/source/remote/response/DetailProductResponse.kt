package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailProductResponse(

	@field:SerializedName("detailMerchandise")
	val detailMerchandise: DetailMerchandise? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DetailMerchandise(

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
