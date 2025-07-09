package com.naufal.kidsnesia.auth.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class VerifyOtpRequest(

	@field:SerializedName("otp")
	val otp: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
