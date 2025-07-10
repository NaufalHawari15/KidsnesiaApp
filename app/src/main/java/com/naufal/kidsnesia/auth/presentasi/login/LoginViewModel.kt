package com.naufal.kidsnesia.auth.presentasi.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.LoginResponse
import com.naufal.kidsnesia.auth.domain.model.UserModel
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okio.IOException
import org.json.JSONObject
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class LoginViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> get() = _loginResult
    val loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Loading())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _validationErrors = MutableLiveData<Map<String, String>>()
    val validationErrors: LiveData<Map<String, String>> get() = _validationErrors

    fun login(email: String, password: String) {
        // Client-side validation
        val errors = validateInput(email, password)
        if (errors.isNotEmpty()) {
            _validationErrors.postValue(errors)
            return
        }

        viewModelScope.launch {
            _isLoading.postValue(true)
            _validationErrors.postValue(emptyMap())

            userUseCase.login(email, password)
                .onStart {
                    _loginResult.postValue(Resource.Loading())
                    loginState.value = Resource.Loading()
                    delay(300) // UX delay
                }
                .catch { e ->
                    val errorMessage = handleException(e)
                    _loginResult.postValue(Resource.Error(errorMessage))
                    loginState.value = Resource.Error(errorMessage)
                }
                .collect { result ->
                    _loginResult.postValue(result)
                    loginState.value = result

                    if (result is Resource.Success) {
                        result.data?.loginResult?.let { loginData ->
                            val user = UserModel(
                                email = loginData.email.orEmpty(),
                                token = loginData.token.orEmpty(),
                                isLogin = true
                            )
                            saveSession(user)
                        }
                    }
                }

            _isLoading.postValue(false)
        }
    }

    private fun validateInput(email: String, password: String): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        if (email.isBlank()) {
            errors["email"] = "Email harus diisi"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            errors["email"] = "Format email tidak valid"
        }

        if (password.isBlank()) {
            errors["password"] = "Kata sandi harus diisi"
        } else if (password.length < 6) {
            errors["password"] = "Kata sandi minimal 6 karakter"
        }

        return errors
    }

    fun clearValidationErrors() {
        _validationErrors.postValue(emptyMap())
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            Log.d("SessionCheck", "Saving session for user: ${user.email}, token: ${user.token}")
            userUseCase.saveSession(user)
        }
    }

    private fun handleException(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    400 -> parseErrorResponse(e.response()?.errorBody()?.string())
                        ?: "Data tidak valid"
                    401 -> "Email atau kata sandi salah"
                    403 -> "Akses ditolak"
                    404 -> "Akun tidak ditemukan"
                    422 -> "Data tidak dapat diproses"
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
            Log.e("LoginViewModel", "Error parsing error response: ${e.message}")
            null
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.coroutineContext.cancelChildren()
    }
}
