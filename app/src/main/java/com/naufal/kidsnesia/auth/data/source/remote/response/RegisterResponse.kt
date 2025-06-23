package com.naufal.kidsnesia.auth.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("registerResult")
	val registerResult: RegisterResult? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class RegisterResult(

	@field:SerializedName("token_verifikasi")
	val tokenVerifikasi: String? = null,

	@field:SerializedName("namaPelanggan")
	val namaPelanggan: String? = null,

	@field:SerializedName("otp")
	val otp: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)
