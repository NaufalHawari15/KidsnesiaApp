package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PilihBankRequest(

	@field:SerializedName("bankPengirim")
	val bankPengirim: String? = null
)
