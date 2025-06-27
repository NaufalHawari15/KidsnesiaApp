package com.naufal.kidsnesia.main_features.domain.repository

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse
import kotlinx.coroutines.flow.Flow

interface IEventRepository {
    fun listEvent(): Flow<Resource<EventResponse>>

    fun detailEvent(idEvent: String): Flow<Resource<DetailEventResponse>>

    fun detailProduct(idMerch: String): Flow<Resource<DetailProductResponse>>

    fun listProduct(): Flow<Resource<ProductResponse>>

    suspend fun getListVideo(token: String): ListVideoResponse

    suspend fun getDetailVideo(token: String, idEvent: String): DetailVideoResponse
}