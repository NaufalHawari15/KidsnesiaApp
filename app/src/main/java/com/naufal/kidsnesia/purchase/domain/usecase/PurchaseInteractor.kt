package com.naufal.kidsnesia.purchase.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import com.naufal.kidsnesia.purchase.domain.repository.IPurchaseRepository
import kotlinx.coroutines.flow.Flow

class PurchaseInteractor(private val purchaseRepository: IPurchaseRepository) : PurchaseUseCase {
    override suspend fun createCart(token: String, request: CartRequest): CartResponse {
        return purchaseRepository.creatCart(token, request)
    }

    override fun getListCart(): Flow<Resource<ListCartResponse>> = purchaseRepository.getListCart()
}