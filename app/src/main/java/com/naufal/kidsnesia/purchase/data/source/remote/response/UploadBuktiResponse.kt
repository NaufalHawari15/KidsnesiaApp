package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UploadBuktiResponse(

	@field:SerializedName("urlBuktiBayarEvent")
	val urlBuktiBayarEvent: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
