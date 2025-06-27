package com.naufal.kidsnesia.main_features.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ListVideoResponse(

	@field:SerializedName("data")
	val data: List<DataItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataItem(

	@field:SerializedName("idVideo")
	val idVideo: Int? = null,

	@field:SerializedName("thumbnail")
	val thumbnail: String? = null,

	@field:SerializedName("filePath")
	val filePath: String? = null,

	@field:SerializedName("judulVideo")
	val judulVideo: String? = null,

	@field:SerializedName("deskripsiVideo")
	val deskripsiVideo: String? = null
)
