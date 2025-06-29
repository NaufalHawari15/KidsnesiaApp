package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CurrentMembershipResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("error")
	val error: Boolean? = null
)

data class Data(

	@field:SerializedName("tanggalMulai")
	val tanggalMulai: String? = null,

	@field:SerializedName("idMembership")
	val idMembership: Int? = null,

	@field:SerializedName("tanggalBerakhir")
	val tanggalBerakhir: String? = null,

	@field:SerializedName("pembayaranMembership")
	val pembayaranMembership: PembayaranMembership? = null,

	@field:SerializedName("statusMembership")
	val statusMembership: String? = null
)

data class PembayaranMembership(

	@field:SerializedName("bankPengirim")
	val bankPengirim: String? = null,

	@field:SerializedName("statusPembayaranMembership")
	val statusPembayaranMembership: String? = null,

	@field:SerializedName("buktiTransfer")
	val buktiTransfer: String? = null,

	@field:SerializedName("idpembayaranMembership")
	val idpembayaranMembership: Int? = null,

	@field:SerializedName("jumlahTransfer")
	val jumlahTransfer: Int? = null
)