package com.naufal.kidsnesia.auth.presentasi.reset_pass.new_pass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import android.util.Log
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassResponse
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.catch

class ResetPassViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    private val _resetResult = MutableLiveData<Resource<ResetPassResponse>>()
    val resetResult: LiveData<Resource<ResetPassResponse>> get() = _resetResult

    val resetState = MutableStateFlow<Resource<ResetPassResponse>>(Resource.Loading())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _validationErrors = MutableLiveData<Map<String, String>>()
    val validationErrors: LiveData<Map<String, String>> get() = _validationErrors

    fun resetPassword(password: String, confirmPassword: String, token: String) {
        // Client-side validation
        val errors = validateInput(password, confirmPassword)
        if (errors.isNotEmpty()) {
            _validationErrors.postValue(errors)
            return
        }

        viewModelScope.launch {
            _isLoading.postValue(true)
            _validationErrors.postValue(emptyMap())

            // Create request with only password (assuming ResetPassRequest only needs password)
            val request = ResetPassRequest(password, confirmPassword)

            userUseCase.resetPass(request, token)
                .onStart {
                    _resetResult.postValue(Resource.Loading())
                    resetState.value = Resource.Loading()
                    delay(300) // UX delay
                }
                .catch { e ->
                    val errorMessage = handleException(e)
                    _resetResult.postValue(Resource.Error(errorMessage))
                    resetState.value = Resource.Error(errorMessage)
                }
                .collect { result ->
                    _resetResult.postValue(result)
                    resetState.value = result
                }

            _isLoading.postValue(false)
        }
    }

    private fun validateInput(password: String?, confirmPassword: String?): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        val pwd = password?.trim() ?: ""
        val confirmPwd = confirmPassword?.trim() ?: ""

        if (pwd.isBlank()) {
            errors["password"] = "Kata sandi harus diisi"
        } else if (pwd.length < 6) {
            errors["password"] = "Kata sandi minimal 6 karakter"
        }

        if (confirmPwd.isBlank()) {
            errors["confirmPassword"] = "Konfirmasi kata sandi harus diisi"
        } else if (pwd != confirmPwd) {
            errors["confirmPassword"] = "Konfirmasi kata sandi tidak sama"
        }

        return errors
    }

    fun clearValidationErrors() {
        _validationErrors.postValue(emptyMap())
    }

    private fun handleException(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    400 -> parseErrorResponse(e.response()?.errorBody()?.string())
                        ?: "Data tidak valid"
                    401 -> "Token tidak valid atau sudah kadaluarsa"
                    403 -> "Akses ditolak"
                    422 -> "Password tidak dapat diproses"
                    500 -> "Server error, coba lagi nanti"
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
                jsonObj.has("errors") -> {
                    val errors = jsonObj.getJSONObject("errors")
                    val firstError = errors.keys().next()
                    val errorArray = errors.getJSONArray(firstError)
                    errorArray.getString(0)
                }
                else -> null
            }
        } catch (e: Exception) {
            Log.e("ResetPassViewModel", "Error parsing error response: ${e.message}")
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}