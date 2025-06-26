package com.naufal.kidsnesia.purchase.data.source

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiResponse
import com.naufal.kidsnesia.purchase.data.source.remote.PurchaseRemoteDataSource
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
import com.naufal.kidsnesia.purchase.domain.repository.IPurchaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody

class PurchaseRepository(
    private val purchaseRemoteDataSource: PurchaseRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) : IPurchaseRepository {

    override suspend fun creatCart(token: String, request: CartRequest): CartResponse {
        return purchaseRemoteDataSource.createCart(token, request)
    }

    override suspend fun createCartMerch(token: String, request: CartMerchRequest): CartMerchResponse {
        return purchaseRemoteDataSource.createCartMerch(token, request)
    }

    override suspend fun getListCart(token: String): ListCartResponse {
        return purchaseRemoteDataSource.getListCart(token)
    }

    override suspend fun getListMerchCart(token: String): ListCartMerchResponse {
        return purchaseRemoteDataSource.getListMerchCart(token)
    }

    override suspend fun getDetailCart(token: String, idPembelianEvent: String): DetailCartResponse {
        return purchaseRemoteDataSource.getDetailCart(token, idPembelianEvent)
    }

    override suspend fun getDetailCartMerch(token: String, idPembelianMerch: String): DetailCartMerchResponse {
        return purchaseRemoteDataSource.getDetailCartMerch(token, idPembelianMerch)
    }

    override suspend fun createCheckout(token: String, idPembelianEvent: String, request: CheckoutRequest): CheckoutResponse {
        return purchaseRemoteDataSource.createCheckout(token, idPembelianEvent, request)
    }

    override suspend fun pilihBank(token: String, idPembelianEvent: String, request: PilihBankRequest): PilihBankResponse {
        return purchaseRemoteDataSource.pilihBank(token, idPembelianEvent, request)
    }

    override suspend fun uploadBukti(token: String, idPembayaranEvent: String, file: MultipartBody.Part): UploadBuktiResponse {
        return purchaseRemoteDataSource.uploadBukti(token, idPembayaranEvent, file)
    }
}