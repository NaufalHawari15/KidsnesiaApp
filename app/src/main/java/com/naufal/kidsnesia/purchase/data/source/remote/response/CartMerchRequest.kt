package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CartMerchRequest(

	@field:SerializedName("itemsMerch")
	val itemsMerch: List<ItemsMerchItem?>? = null
)

data class ItemsMerchItem(

	@field:SerializedName("jumlah")
	val jumlah: Int? = null,

	@field:SerializedName("idMerch")
	val idMerch: Int? = null
)
