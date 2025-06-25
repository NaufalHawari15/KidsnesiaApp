package com.naufal.kidsnesia.purchase.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import kotlinx.coroutines.flow.Flow

interface PurchaseUseCase {
    suspend fun createCart(token: String, request: CartRequest): CartResponse

    suspend fun createCartMerch(token: String, request: CartMerchRequest): CartMerchResponse

    suspend fun getListCart(token: String): ListCartResponse

    suspend fun getListMerchCart(token: String): ListCartMerchResponse
//    fun getListCart(): Flow<Resource<ListCartResponse>>
}