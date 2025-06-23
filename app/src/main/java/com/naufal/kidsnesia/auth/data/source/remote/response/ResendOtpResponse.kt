package com.naufal.kidsnesia.auth.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ResendOtpResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("resendResult")
	val resendResult: ResendResult? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class ResendResult(

	@field:SerializedName("token_verifikasi")
	val tokenVerifikasi: String? = null,

	@field:SerializedName("otp")
	val otp: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
