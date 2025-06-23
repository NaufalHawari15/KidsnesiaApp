package com.naufal.kidsnesia.purchase.domain.repository

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import kotlinx.coroutines.flow.Flow

interface IPurchaseRepository {
    suspend fun creatCart(token: String, request: CartRequest): CartResponse

    fun getListCart(): Flow<Resource<ListCartResponse>>
}