package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UploadBuktiMembershipResponse(

	@field:SerializedName("urlBuktiTransferMembership")
	val urlBuktiTransferMembership: String? = null,

	@field:SerializedName("waktuTransfer")
	val waktuTransfer: String? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
