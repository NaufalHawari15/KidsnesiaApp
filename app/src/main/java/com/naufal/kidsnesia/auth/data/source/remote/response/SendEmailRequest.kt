package com.naufal.kidsnesia.auth.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class SendEmailRequest(

	@field:SerializedName("email")
	val email: String? = null
)
