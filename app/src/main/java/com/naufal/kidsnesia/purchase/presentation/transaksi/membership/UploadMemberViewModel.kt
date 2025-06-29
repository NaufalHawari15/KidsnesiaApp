package com.naufal.kidsnesia.purchase.presentation.transaksi.membership

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.UploadBuktiMembershipResponse
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import com.naufal.kidsnesia.util.FileUtil
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadMemberViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _uploadResult = MutableLiveData<UploadBuktiMembershipResponse>()
    val uploadResult: LiveData<UploadBuktiMembershipResponse> = _uploadResult

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun uploadBuktiMembership(idPembayaran: String, file: File) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val token = authLocalDataSource.getToken()
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val multipartBody = MultipartBody.Part.createFormData(
                    "bukti_transfer",  // sesuai dengan backend
                    file.name,
                    requestFile
                )

                val response = purchaseUseCase.uploadBuktiMembership(
                    token = "Bearer $token",
                    idPembayaranMembership = idPembayaran,
                    file = multipartBody
                )
                _uploadResult.value = response
            } catch (e: Exception) {
                Log.e("UploadVM", "Upload error: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }
}