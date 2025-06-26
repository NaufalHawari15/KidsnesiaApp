package com.naufal.kidsnesia.purchase.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
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
import okhttp3.MultipartBody

class PurchaseInteractor(private val purchaseRepository: IPurchaseRepository) : PurchaseUseCase {
    override suspend fun createCart(token: String, request: CartRequest): CartResponse {
        return purchaseRepository.creatCart(token, request)
    }

    override suspend fun createCartMerch(token: String, request: CartMerchRequest): CartMerchResponse {
        return purchaseRepository.createCartMerch(token, request)
    }

    override suspend fun getListCart(token: String): ListCartResponse {
        return purchaseRepository.getListCart(token)
    }

    override suspend fun getListMerchCart(token: String): ListCartMerchResponse {
        return purchaseRepository.getListMerchCart(token)
    }

    override suspend fun getDetailCart(token: String, idPembelianEvent: String): DetailCartResponse {
        return purchaseRepository.getDetailCart(token, idPembelianEvent)
    }

    override suspend fun getDetailCartMerch(token: String, idPembelianMerch: String): DetailCartMerchResponse {
        return purchaseRepository.getDetailCartMerch(token, idPembelianMerch)
    }

    override suspend fun createCheckout(token: String, idPembelianEvent: String, request: CheckoutRequest): CheckoutResponse {
        return purchaseRepository.createCheckout(token, idPembelianEvent, request)
    }

    override suspend fun pilihBank(token: String, idPembelianEvent: String, request: PilihBankRequest): PilihBankResponse {
        return purchaseRepository.pilihBank(token, idPembelianEvent, request)
    }

    override suspend fun uploadBukti(token: String, idPembayaranEvent: String, file: MultipartBody.Part): UploadBuktiResponse {
        return purchaseRepository.uploadBukti(token, idPembayaranEvent, file)
    }
}