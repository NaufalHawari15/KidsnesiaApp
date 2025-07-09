package com.naufal.kidsnesia.auth.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResetPassResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
