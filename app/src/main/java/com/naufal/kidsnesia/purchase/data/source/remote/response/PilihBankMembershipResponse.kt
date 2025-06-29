package com.naufal.kidsnesia.purchase.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class PilihBankMembershipResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Data(

	@field:SerializedName("statusPembayaranMembership")
	val statusPembayaranMembership: String? = null,

	@field:SerializedName("idMembership")
	val idMembership: Int? = null,

	@field:SerializedName("tanggalPembelian")
	val tanggalPembelian: String? = null,

	@field:SerializedName("jumlahTransfer")
	val jumlahTransfer: Int? = null,

	@field:SerializedName("idPembayaranMembership")
	val idPembayaranMembership: Int? = null,

	@field:SerializedName("atasNama")
	val atasNama: String? = null,

	@field:SerializedName("namaBankTujuan")
	val namaBankTujuan: String? = null,

	@field:SerializedName("noRekeningTujuan")
	val noRekeningTujuan: String? = null
)
