package com.naufal.kidsnesia.purchase.presentation.transaksi.membership

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMembershipRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMembershipResponse
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import com.naufal.kidsnesia.util.FileUtil
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class TransaksiMembershipViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _pilihBankResult = MutableLiveData<Result<PilihBankMembershipResponse>>()
    val pilihBankResult: LiveData<Result<PilihBankMembershipResponse>> = _pilihBankResult

    fun pilihBankMembership(namaBank: String) {
        viewModelScope.launch {
            try {
                val token = authLocalDataSource.getToken()
                val request = PilihBankMembershipRequest(bankPengirim = namaBank)
                val response = purchaseUseCase.pilihBankMembership("Bearer $token", request)
                _pilihBankResult.postValue(Result.success(response))
            } catch (e: Exception) {
                _pilihBankResult.postValue(Result.failure(e))
            }
        }
    }
}