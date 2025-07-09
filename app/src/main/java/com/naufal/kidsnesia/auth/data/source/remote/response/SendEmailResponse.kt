package com.naufal.kidsnesia.auth.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SendEmailResponse(

	@field:SerializedName("otp")
	val otp: Int? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
