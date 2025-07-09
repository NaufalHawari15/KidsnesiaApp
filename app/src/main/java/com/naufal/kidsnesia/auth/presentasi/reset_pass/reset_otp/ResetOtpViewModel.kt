package com.naufal.kidsnesia.auth.presentasi.reset_pass.reset_otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpResponse
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class ResetOtpViewModel(private val userUseCase: UserUseCase) : ViewModel(){
    private val _verifyOtpResult = MutableLiveData<Resource<VerifyOtpResponse>>()
    val verifyOtpResult: LiveData<Resource<VerifyOtpResponse>> get() = _verifyOtpResult

    fun verifyOtp(request: VerifyOtpRequest) {
        viewModelScope.launch {
            userUseCase.verifyResetOtp(request).collect {
                _verifyOtpResult.value = it
            }
        }
    }

    fun resendOtp(request: SendEmailRequest) {
        viewModelScope.launch {
            userUseCase.sendEmail(request).collect {
                if (it is Resource.Success) {
                    // Optional: Tampilkan pesan OTP terkirim ulang
                }
            }
        }
    }

}