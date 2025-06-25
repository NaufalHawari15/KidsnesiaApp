package com.naufal.kidsnesia.purchase.presentation.transaksi.event

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankRequest
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class TransaksiViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _result = MutableLiveData<Resource<Unit>>()
    val result: LiveData<Resource<Unit>> = _result

    fun konfirmasiPembayaran(idPembayaranEvent: String, namaBank: String, imageFile: File) {
        viewModelScope.launch {
            try {
                _result.value = Resource.Loading()
                val token = authLocalDataSource.getToken()

                // 1. Pilih Bank
                val pilihBankRequest = PilihBankRequest(namaBank = namaBank, idPembayaranEvent = idPembayaranEvent.toInt())
                purchaseUseCase.pilihBank("Bearer $token", pilihBankRequest)

                // 2. Upload Bukti
                val requestFile = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", imageFile.name, requestFile)
                purchaseUseCase.uploadBukti("Bearer $token", body)

                _result.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _result.value = Resource.Error(e.message ?: "Terjadi kesalahan saat konfirmasi")
            }
        }
    }
}
