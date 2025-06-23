package com.naufal.kidsnesia.purchase.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import kotlinx.coroutines.flow.Flow

interface PurchaseUseCase {
    suspend fun createCart(token: String, request: CartRequest): CartResponse

    fun getListCart(): Flow<Resource<ListCartResponse>>
}