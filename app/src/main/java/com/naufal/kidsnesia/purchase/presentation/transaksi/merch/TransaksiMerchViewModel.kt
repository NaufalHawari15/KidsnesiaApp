package com.naufal.kidsnesia.purchase.presentation.transaksi.merch

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMerchRequest
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import com.naufal.kidsnesia.util.FileUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class TransaksiMerchViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun konfirmasiPembayaran(
        idPembayaranMerch: String,
        namaBank: String,
        buktiUri: Uri,
        context: Context
    ) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                val token = authLocalDataSource.getToken()

                val pilihBankMerchRequest = PilihBankMerchRequest(bankPengirim = namaBank)
                val pilihBankMerchResponse = purchaseUseCase.pilihBankMerch(
                    "Bearer $token", idPembayaranMerch, pilihBankMerchRequest
                )

                val idPembayaranMerch = pilihBankMerchResponse.dataPembayaranMerch?.idPembayaranMerch.toString()
                if (idPembayaranMerch.isEmpty()) throw Exception("ID Pembayaran tidak ditemukan")

                val file = FileUtil.from(context, buktiUri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("buktiBayarMerch", file.name, requestFile)

                purchaseUseCase.uploadBuktiMerch("Bearer $token", idPembayaranMerch, part)

                _success.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _success.value = false
            } finally {
                _loadingState.value = false
            }
        }
    }
}