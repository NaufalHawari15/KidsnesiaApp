package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CheckoutMerchRequest(

	@field:SerializedName("itemsMerch")
	val itemsMerch: List<ItemsMerch?>? = null
)

data class ItemsMerch(

	@field:SerializedName("idMerch")
	val idMerch: Int? = null,

	@field:SerializedName("jumlah")
	val jumlah: Int? = null
)

