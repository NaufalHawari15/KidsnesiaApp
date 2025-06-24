package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CartMerchResponse(

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("pembelianMerchResponse")
	val pembelianMerchResponse: PembelianMerchResponse? = null
)

data class PembelianMerchResponse(

	@field:SerializedName("totalHargaMerch")
	val totalHargaMerch: Int? = null,

	@field:SerializedName("statusPembelianMerch")
	val statusPembelianMerch: String? = null,

	@field:SerializedName("idPembelianMerch")
	val idPembelianMerch: Int? = null,

	@field:SerializedName("cartMerchItem")
	val cartMerchItem: List<CartMerchItemItem?>? = null,

	@field:SerializedName("tanggalPembelianMerch")
	val tanggalPembelianMerch: Any? = null
)

data class CartMerchItemItem(

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
