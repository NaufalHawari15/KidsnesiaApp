package com.naufal.kidsnesia.auth.presentasi.reset_pass.reset_otp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpResponse
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class ResetOtpViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    private val _verifyOtpResult = MutableLiveData<Resource<VerifyOtpResponse>>()
    val verifyOtpResult: LiveData<Resource<VerifyOtpResponse>> get() = _verifyOtpResult

    private val _resendResult = MutableLiveData<Resource<SendEmailResponse>>()
    val resendResult: LiveData<Resource<SendEmailResponse>> get() = _resendResult

    fun verifyOtp(request: VerifyOtpRequest) {
        Log.d("ResetOtpViewModel", "Verifying OTP: $request")
        viewModelScope.launch {
            userUseCase.verifyResetOtp(request).collect { result ->
                Log.d("ResetOtpViewModel", "Verify OTP result: $result")
                _verifyOtpResult.value = result
            }
        }
    }

    fun resendOtp(request: SendEmailRequest) {
        Log.d("ResetOtpViewModel", "Resending OTP to: ${request.email}")
        viewModelScope.launch {
            userUseCase.sendEmail(request).collect { result ->
                Log.d("ResetOtpViewModel", "Resend OTP result: $result")
                _resendResult.value = result
            }
        }
    }
}
