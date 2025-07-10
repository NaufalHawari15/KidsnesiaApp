package com.naufal.kidsnesia.auth.presentasi.reset_pass.confirm_email

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailResponse
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException

class ConfirmEmailViewModel (private val userUseCase: UserUseCase) : ViewModel() {
    private val _confirmEmailResult = MutableLiveData<Resource<SendEmailResponse>>()
    val confirmEmailResult: LiveData<Resource<SendEmailResponse>> get() = _confirmEmailResult

    val confirmState = MutableStateFlow<Resource<SendEmailResponse>>(Resource.Loading())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun sendEmail(request: SendEmailRequest) {
        Log.d("ConfirmEmailViewModel", "Sending email request: $request")
        _isLoading.value = true

        userUseCase.sendEmail(request)
            .onEach { result ->
                Log.d("ConfirmEmailViewModel", "Received result: $result")
                _confirmEmailResult.value = result
                _isLoading.value = result is Resource.Loading
            }
            .catch { e ->
                Log.e("ConfirmEmailViewModel", "Unexpected error in ViewModel", e)
                _confirmEmailResult.value = Resource.Error("Terjadi kesalahan tidak terduga")
                _isLoading.value = false
            }
            .launchIn(viewModelScope)
    }
}


