package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CartRequest(

	@field:SerializedName("itemsEvent")
	val itemsEvent: List<ItemsEventItem?>? = null
)

data class ItemsEventItem(

	@field:SerializedName("idEvent")
	val idEvent: Int? = null,

	@field:SerializedName("jumlahTiket")
	val jumlahTiket: Int? = null
)
