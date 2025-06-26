package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UploadBuktiMerchResponse(

	@field:SerializedName("urlBuktiBayarMerch")
	val urlBuktiBayarMerch: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
