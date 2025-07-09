package com.naufal.kidsnesia.auth.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VerifyOtpResponse(

	@field:SerializedName("resetResult")
	val resetResult: ResetResult? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class ResetResult(

	@field:SerializedName("token_reset")
	val tokenReset: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
