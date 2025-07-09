package com.naufal.kidsnesia.main_features.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.CurrentMembershipResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailNotaEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailNotaMerchResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListVideoResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaEventResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaMerchResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse
import kotlinx.coroutines.flow.Flow

interface EventUseCase {
    fun listEvent(): Flow<Resource<EventResponse>>

    fun detailEvent(idEvent:String): Flow<Resource<DetailEventResponse>>

    fun detailProduct(idMerch: String): Flow<Resource<DetailProductResponse>>

    fun listProduct(): Flow<Resource<ProductResponse>>

    suspend fun getListVideo(token: String): ListVideoResponse

    suspend fun getDetailVideo(token: String, idEvent: String): DetailVideoResponse

    suspend fun getMembership(token: String): CurrentMembershipResponse

    suspend fun getNotaEvent(token: String): NotaEventResponse

    suspend fun getNotaMerch(token: String): NotaMerchResponse

    suspend fun getDetailNotaEvent(token: String, idPembelianEvent: String): DetailNotaEventResponse

    suspend fun getDetailNotaMerch(token: String, idPembelianMerch: String): DetailNotaMerchResponse
}