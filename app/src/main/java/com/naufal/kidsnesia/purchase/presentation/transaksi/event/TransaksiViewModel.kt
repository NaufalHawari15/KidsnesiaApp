package com.naufal.kidsnesia.purchase.presentation.transaksi.event

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankRequest
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import com.naufal.kidsnesia.util.FileUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class TransaksiViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean> = _success

    fun konfirmasiPembayaran(
        idPembayaranEvent: String,
        namaBank: String,
        buktiUri: Uri,
        context: Context
    ) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                val token = authLocalDataSource.getToken()

                // 🔁 Gunakan idPembelianEvent untuk pilihBank
                val pilihBankRequest = PilihBankRequest(bankPengirim = namaBank)
                val pilihBankResponse = purchaseUseCase.pilihBank(
                    "Bearer $token", idPembayaranEvent, pilihBankRequest
                )

                val idPembayaran = pilihBankResponse.dataPembayaranEvent?.idPembayaranEvent.toString()
                if (idPembayaran.isEmpty()) throw Exception("ID Pembayaran tidak ditemukan")

                // Upload bukti transfer
                val file = FileUtil.from(context, buktiUri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData("buktiBayarEvent", file.name, requestFile)

                purchaseUseCase.uploadBukti("Bearer $token", idPembayaran, part)


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


