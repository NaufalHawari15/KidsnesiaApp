package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CheckoutRequest(

	@field:SerializedName("itemsEvent")
	val itemsEvent: List<ItemsEvent?>? = null
)

data class ItemsEvent(

	@field:SerializedName("idEvent")
	val idEvent: Int? = null,

	@field:SerializedName("jumlahTiket")
	val jumlahTiket: Int? = null
)
