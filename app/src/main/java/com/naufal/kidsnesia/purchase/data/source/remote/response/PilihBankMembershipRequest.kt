package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PilihBankMembershipRequest(

	@field:SerializedName("bank_pengirim")
	val bankPengirim: String? = null
)
