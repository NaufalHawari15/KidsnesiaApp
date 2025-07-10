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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class OtpViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _otpResult = MutableLiveData<Resource<OtpResponse>>()
    val otpResult: LiveData<Resource<OtpResponse>> get() = _otpResult

    private val _resendResult = MutableLiveData<Resource<ResendOtpResponse>>()
    val resendResult: LiveData<Resource<ResendOtpResponse>> get() = _resendResult

    fun verifyOtp(otp: String, tokenVerifikasi: String) {
        viewModelScope.launch {
            userUseCase.verifyOtp(OtpRequest(otp), tokenVerifikasi)
                .onStart {
                    _otpResult.postValue(Resource.Loading())
                    delay(300)
                }
                .catch { e ->
                    val errorMessage = handleException(e)
                    _otpResult.postValue(Resource.Error(errorMessage))
                }
                .collect { result ->
                    _otpResult.postValue(result)
                }
        }
    }

    private fun handleException(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    400, 401, 422 -> parseErrorResponse(e.response()?.errorBody()?.string())
                        ?: "Kode OTP salah atau sudah tidak berlaku"
                    500 -> "Server sedang bermasalah"
                    else -> "Kesalahan server (${e.code()})"
                }
            }
            is IOException -> "Tidak ada koneksi internet"
            is SocketTimeoutException -> "Waktu koneksi habis"
            is UnknownHostException -> "Gagal terhubung ke server"
            else -> e.localizedMessage ?: "Terjadi kesalahan"
        }
    }

    private fun parseErrorResponse(errorBody: String?): String? {
        return try {
            if (errorBody.isNullOrEmpty()) return null
            val jsonObj = JSONObject(errorBody)
            when {
                jsonObj.has("message") -> jsonObj.getString("message")
                jsonObj.has("error") -> jsonObj.getString("error")
                else -> null
            }
        } catch (e: Exception) {
            null
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
