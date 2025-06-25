package com.naufal.kidsnesia.purchase.data.source.remote

import com.naufal.kidsnesia.auth.data.source.remote.network.ApiResponse
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiService
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class PurchaseRemoteDataSource(private val apiService: ApiService) {
    suspend fun createCart(token: String, request: CartRequest): CartResponse {
        return apiService.createCart(token, request)
    }

    suspend fun createCartMerch(token: String, request: CartMerchRequest): CartMerchResponse {
        return apiService.createMerchCart(token, request)
    }

    suspend fun getListCart(token: String): ListCartResponse {
        return apiService.getListCart(token)
    }

    suspend fun getListMerchCart(token: String): ListCartMerchResponse {
        return apiService.getListMerchCart(token)
    }
//    suspend fun getListCart(): Flow<ApiResponse<ListCartResponse>> = flow {
//        try {
//            val response = apiService.getListCart()
//            emit(ApiResponse.Success(response))
//        } catch (e: Exception) {
//            emit(ApiResponse.Error(e.localizedMessage ?: "Unknown Error"))
//        }
//    }.flowOn(Dispatchers.IO)
}