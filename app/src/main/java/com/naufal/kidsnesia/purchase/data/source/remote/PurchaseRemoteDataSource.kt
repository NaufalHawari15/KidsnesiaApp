package com.naufal.kidsnesia.purchase.data.source.remote

import com.naufal.kidsnesia.auth.data.source.remote.network.ApiResponse
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiService
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.DetailCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.DetailCartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.UploadBuktiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody

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

    suspend fun getDetailCart(token: String, idPembelianEvent: String): DetailCartResponse {
        return apiService.getDetailCart(token, idPembelianEvent)
    }

    suspend fun getDetailCartMerch(token: String, idPembelianMerch: String): DetailCartMerchResponse {
        return apiService.getDetailCartMerch(token, idPembelianMerch)
    }

    suspend fun createCheckout(token: String, idPembelianEvent: String ,request: CheckoutRequest): CheckoutResponse {
        return apiService.createCheckout(token, idPembelianEvent, request)
    }

    suspend fun pilihBank(token: String, request: PilihBankRequest): PilihBankResponse {
        return apiService.pilihBank(token, request)
    }

    suspend fun uploadBukti(token: String, request: RequestBody): UploadBuktiResponse {
        return apiService.uploadBukti(token, request)
    }
}