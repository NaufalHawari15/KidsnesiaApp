package com.naufal.kidsnesia.auth.presentasi.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.ResendOtpResponse
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class OtpViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _otpResult = MutableLiveData<Resource<OtpResponse>>()
    val otpResult: LiveData<Resource<OtpResponse>> get() = _otpResult

    private val _resendResult = MutableLiveData<Resource<ResendOtpResponse>>()
    val resendResult: LiveData<Resource<ResendOtpResponse>> get() = _resendResult

    fun verifyOtp(otp: String, tokenVerifikasi: String) {
        viewModelScope.launch {
            userUseCase.verifyOtp(OtpRequest(otp), tokenVerifikasi)
                .onStart { _otpResult.postValue(Resource.Loading()) }
                .catch { e -> _otpResult.postValue(Resource.Error(e.message ?: "Error")) }
                .collect { _otpResult.postValue(it) }
        }
    }

    fun resendOtp(tokenVerifikasi: String) {
        viewModelScope.launch {
            userUseCase.resendOtp(tokenVerifikasi)
                .onStart { _resendResult.postValue(Resource.Loading()) }
                .catch { e -> _resendResult.postValue(Resource.Error(e.message ?: "Error")) }
                .collect { _resendResult.postValue(it) }
        }
    }
}
