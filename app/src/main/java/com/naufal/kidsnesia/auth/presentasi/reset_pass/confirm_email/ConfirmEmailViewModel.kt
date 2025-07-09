package com.naufal.kidsnesia.auth.presentasi.reset_pass.confirm_email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailResponse
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ConfirmEmailViewModel (private val userUseCase: UserUseCase) : ViewModel() {
    private val _confirmEmailResult = MutableLiveData<Resource<SendEmailResponse>>()
    val confirmEmailResult: LiveData<Resource<SendEmailResponse>> get() = _confirmEmailResult
    val confirmState = MutableStateFlow<Resource<SendEmailResponse>>(Resource.Loading())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun sendEmail(request: SendEmailRequest) {
        viewModelScope.launch {
            userUseCase.sendEmail(request).collect {
                _confirmEmailResult.value = it
            }
        }
    }

}