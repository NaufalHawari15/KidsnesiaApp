package com.naufal.kidsnesia.auth.presentasi.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterResponse
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RegisterViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _registerResult = MutableLiveData<Resource<RegisterResponse>>()
    val registerResult: LiveData<Resource<RegisterResponse>> get() = _registerResult
    val registerState = MutableStateFlow<Resource<RegisterResponse>>(Resource.Loading())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun register(namaPelanggan: String, email: String, password: String, noHpPelanggan: String) {
        viewModelScope.launch {
            _isLoading.postValue(true) // Tampilkan loading

            userUseCase.register(namaPelanggan, email, password, noHpPelanggan)
                .onStart {
                    _registerResult.postValue(Resource.Loading())
                    registerState.value = Resource.Loading()
                    delay(200)
                }
                .catch { e ->
                    _registerResult.postValue(Resource.Error(e.localizedMessage ?: "Unknown Error"))
                    registerState.value = Resource.Error(e.localizedMessage ?: "Unknown Error")
                }
                .collect { result ->
                    _registerResult.postValue(result)
                    registerState.value = result
                }

            _isLoading.postValue(false) // Sembunyikan loading setelah selesai
        }
    }
}