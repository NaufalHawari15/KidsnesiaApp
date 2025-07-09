package com.naufal.kidsnesia.main_features.data.source.remote

import android.util.Log
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiResponse
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiService
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EventRemoteDataSource(private val apiService: ApiService) {

    fun getEvent(): Flow<ApiResponse<EventResponse>> {
        return flow {
            try {
                val response = apiService.getEvent()
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetail(idEvent: String): Flow<ApiResponse<DetailEventResponse>> {
        return flow {
            try {
                val response = apiService.getDetail(idEvent)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)

    }

    fun getProduct(): Flow<ApiResponse<ProductResponse>> {
        return flow {
            try {
                val response = apiService.getProduct()
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getDetailProduct(idMerch: String): Flow<ApiResponse<DetailProductResponse>> {
        return flow {
            try {
                val response = apiService.getDetailProduct(idMerch)
                emit(ApiResponse.Success(response))
            }
            catch (e: Exception) {
                emit(ApiResponse.Error(toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getListVideo(token: String): ListVideoResponse {
        return apiService.getListVideo(token)
    }

    suspend fun getDetailVideo(token: String, idVideo: String): DetailVideoResponse {
        return apiService.getDetailVideo(token, idVideo)
    }

    suspend fun getMembership(token: String): CurrentMembershipResponse {
        return apiService.getMembership(token)
    }

    suspend fun getNotaEvent(token: String): NotaEventResponse {
        return apiService.getNotaEvent(token)
    }

    suspend fun getNotaMerch(token: String): NotaMerchResponse {
        return apiService.getNotaMerch(token)
    }

    suspend fun getDetailNotaEVent(token: String, idPembelianEvent: String): DetailNotaEventResponse {
        return apiService.getDetailNotaEvent(token, idPembelianEvent)
    }

    suspend fun getDetailNotaMerch(token: String, idPembelianMerch: String): DetailNotaMerchResponse {
        return apiService.getDetailNotaMerch(token, idPembelianMerch)
    }
}