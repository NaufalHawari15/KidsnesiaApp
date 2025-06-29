package com.naufal.kidsnesia.purchase.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.DetailCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.DetailCartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMembershipRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMembershipResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.UploadBuktiMembershipResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.UploadBuktiMerchResponse
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

    override suspend fun createCheckoutMerch(token: String, idPembelianMerch: String, request: CheckoutMerchRequest): CheckoutMerchResponse {
        return purchaseRepository.createCheckoutMerch(token, idPembelianMerch, request)
    }

    override suspend fun pilihBank(token: String, idPembelianEvent: String, request: PilihBankRequest): PilihBankResponse {
        return purchaseRepository.pilihBank(token, idPembelianEvent, request)
    }

    override suspend fun uploadBukti(token: String, idPembayaranEvent: String, file: MultipartBody.Part): UploadBuktiResponse {
        return purchaseRepository.uploadBukti(token, idPembayaranEvent, file)
    }

    override suspend fun pilihBankMerch(token: String, idPembayaranMerch: String, request: PilihBankMerchRequest): PilihBankMerchResponse {
        return purchaseRepository.pilihBankMerch(token, idPembayaranMerch, request)
    }

    override suspend fun uploadBuktiMerch(token: String, idPembayaranMerch: String, file: MultipartBody.Part): UploadBuktiMerchResponse {
        return purchaseRepository.uploadBuktiMerch(token, idPembayaranMerch, file)
    }

    override suspend fun pilihBankMembership(token: String, request: PilihBankMembershipRequest): PilihBankMembershipResponse {
        return purchaseRepository.pilihBankMembership(token, request)
    }

    override suspend fun uploadBuktiMembership(token: String, idPembayaranMembership: String, file: MultipartBody.Part): UploadBuktiMembershipResponse {
        return purchaseRepository.uploadBuktiMembership(token, idPembayaranMembership, file)
    }
}