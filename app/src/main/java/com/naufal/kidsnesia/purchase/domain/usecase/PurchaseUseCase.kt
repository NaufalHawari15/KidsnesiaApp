package com.naufal.kidsnesia.purchase.domain.usecase

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
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface PurchaseUseCase {
    suspend fun createCart(token: String, request: CartRequest): CartResponse

    suspend fun createCartMerch(token: String, request: CartMerchRequest): CartMerchResponse

    suspend fun getListCart(token: String): ListCartResponse

    suspend fun getListMerchCart(token: String): ListCartMerchResponse

    suspend fun getDetailCart(token: String, idPembelianEvent: String): DetailCartResponse

    suspend fun getDetailCartMerch(token: String, idPembelianMerch: String): DetailCartMerchResponse

    suspend fun createCheckout(token: String, idPembelianEvent: String, request: CheckoutRequest): CheckoutResponse

    suspend fun pilihBank(token: String, idPembelianEvent: String, request: PilihBankRequest): PilihBankResponse

    suspend fun uploadBukti(token: String, idPembayaranEvent: String, file: MultipartBody.Part): UploadBuktiResponse
}