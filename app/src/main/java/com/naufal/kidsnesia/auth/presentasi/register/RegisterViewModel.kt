package com.naufal.kidsnesia.auth.presentasi.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterResponse
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

class RegisterViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _registerResult = MutableLiveData<Resource<RegisterResponse>>()
    val registerResult: LiveData<Resource<RegisterResponse>> get() = _registerResult
    val registerState = MutableStateFlow<Resource<RegisterResponse>>(Resource.Loading())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _validationErrors = MutableLiveData<Map<String, String>>()
    val validationErrors: LiveData<Map<String, String>> get() = _validationErrors

    fun register(namaPelanggan: String, email: String, password: String, noHpPelanggan: String) {
        // Client-side validation
        val errors = validateInput(namaPelanggan, email, password, noHpPelanggan)
        if (errors.isNotEmpty()) {
            _validationErrors.postValue(errors)
            return
        }

        viewModelScope.launch {
            _isLoading.postValue(true)
            _validationErrors.postValue(emptyMap()) // Clear previous errors

            userUseCase.register(namaPelanggan, email, password, noHpPelanggan)
                .onStart {
                    _registerResult.postValue(Resource.Loading())
                    registerState.value = Resource.Loading()
                    // Add slight delay for better UX
                    delay(300)
                }
                .catch { e ->
                    val errorMessage = handleException(e)
                    _registerResult.postValue(Resource.Error(errorMessage))
                    registerState.value = Resource.Error(errorMessage)
                }
                .collect { result ->
                    _registerResult.postValue(result)
                    registerState.value = result
                }

            _isLoading.postValue(false)
        }
    }

    private fun validateInput(
        name: String,
        email: String,
        password: String,
        phone: String
    ): Map<String, String> {
        val errors = mutableMapOf<String, String>()

        // Name validation
        when {
            name.isBlank() -> errors["name"] = "Nama lengkap harus diisi"
            name.length < 2 -> errors["name"] = "Nama minimal 2 karakter"
            name.length > 50 -> errors["name"] = "Nama maksimal 50 karakter"
            !name.matches(Regex("^[a-zA-Z\\s.'-]+$")) -> errors["name"] = "Nama hanya boleh mengandung huruf"
        }

        // Email validation
        when {
            email.isBlank() -> errors["email"] = "Email harus diisi"
            !isValidEmail(email) -> errors["email"] = "Format email tidak valid"
            email.length > 100 -> errors["email"] = "Email terlalu panjang"
        }

        // Password validation
        when {
            password.isBlank() -> errors["password"] = "Kata sandi harus diisi"
            password.length < 6 -> errors["password"] = "Kata sandi minimal 6 karakter"
            password.length > 128 -> errors["password"] = "Kata sandi maksimal 128 karakter"
            !password.matches(Regex(".*[A-Za-z].*")) -> errors["password"] = "Kata sandi harus mengandung huruf"
        }

        // Phone validation
        when {
            phone.isBlank() -> errors["phone"] = "Nomor telepon harus diisi"
            phone.length < 10 -> errors["phone"] = "Nomor telepon minimal 10 digit"
            phone.length > 15 -> errors["phone"] = "Nomor telepon maksimal 15 digit"
            !phone.matches(Regex("^[0-9+\\-\\s()]*$")) -> errors["phone"] = "Format nomor telepon tidak valid"
        }

        return errors
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun handleException(e: Throwable): String {
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    400 -> parseErrorResponse(e.response()?.errorBody()?.string())
                        ?: "Data yang dimasukkan tidak valid"
                    401 -> "Tidak memiliki akses"
                    403 -> "Akses ditolak"
                    404 -> "Layanan tidak ditemukan"
                    409 -> "Email atau nomor telepon sudah terdaftar"
                    422 -> "Data tidak dapat diproses"
                    429 -> "Terlalu banyak percobaan, coba lagi nanti"
                    500 -> "Server mengalami masalah, coba lagi nanti"
                    503 -> "Layanan sedang tidak tersedia"
                    else -> "Terjadi kesalahan pada server (${e.code()})"
                }
            }
            is IOException -> "Tidak ada koneksi internet"
            is SocketTimeoutException -> "Koneksi timeout, coba lagi"
            is UnknownHostException -> "Tidak dapat terhubung ke server"
            else -> e.localizedMessage ?: "Terjadi kesalahan yang tidak diketahui"
        }
    }

    private fun parseErrorResponse(errorBody: String?): String? {
        return try {
            if (errorBody.isNullOrEmpty()) return null

            val jsonObj = JSONObject(errorBody)

            // Try different possible error message keys
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
            Log.e("RegisterViewModel", "Error parsing error response: ${e.message}")
            null
        }
    }

    fun clearValidationErrors() {
        _validationErrors.postValue(emptyMap())
    }

    override fun onCleared() {
        super.onCleared()
        // Cancel any ongoing operations
        viewModelScope.coroutineContext.cancelChildren()
    }
}