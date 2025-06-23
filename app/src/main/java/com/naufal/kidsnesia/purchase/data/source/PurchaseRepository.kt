package com.naufal.kidsnesia.purchase.data.source

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiResponse
import com.naufal.kidsnesia.purchase.data.source.remote.PurchaseRemoteDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import com.naufal.kidsnesia.purchase.domain.repository.IPurchaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PurchaseRepository(
    private val purchaseRemoteDataSource: PurchaseRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) : IPurchaseRepository {

    override suspend fun creatCart(token: String, request: CartRequest): CartResponse {
        return purchaseRemoteDataSource.createCart(token, request)
    }

    override fun getListCart(): Flow<Resource<ListCartResponse>> = flow {
        emit(Resource.Loading())
        authLocalDataSource.getSession().collect { user->
            if (user.token.isNotEmpty()) {
                purchaseRemoteDataSource.getListCart().collect { apiResponse ->
                    when (apiResponse) {
                        is ApiResponse.Success -> emit(Resource.Success(apiResponse.data))
                        is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                        ApiResponse.Empty -> emit(Resource.Error("Empty response from server"))
                    }
                }
            }
            else {
                emit(Resource.Error("User not logged in"))
            }
        }
    }
}