package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListCartMerchResponse(

	@field:SerializedName("listCartMerch")
	val listCartMerch: List<ListCartMerchItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class ListCartMerchItem(

	@field:SerializedName("totalHargaMerch")
	val totalHargaMerch: Int? = null,

	@field:SerializedName("statusPembelianMerch")
	val statusPembelianMerch: String? = null,

	@field:SerializedName("idPembelianMerch")
	val idPembelianMerch: Int? = null,

	@field:SerializedName("cartMerchItem")
	val cartMerchItem: List<CartMerchItemItems?>? = null,

	@field:SerializedName("tanggalPembelianMerch")
	val tanggalPembelianMerch: Any? = null
)

data class CartMerchItemItems(
	@field:SerializedName("fotoMerchandise")
	val fotoMerchandise: String? = null,

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
