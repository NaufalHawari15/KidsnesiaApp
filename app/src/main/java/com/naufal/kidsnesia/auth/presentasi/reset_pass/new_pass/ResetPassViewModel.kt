package com.naufal.kidsnesia.auth.presentasi.reset_pass.new_pass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassResponse
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class ResetPassViewModel(private val userUseCase: UserUseCase): ViewModel() {
    private val _resetResult = MutableLiveData<Resource<ResetPassResponse>>()
    val resetResult: LiveData<Resource<ResetPassResponse>> get() = _resetResult

    fun resetPassword(request: ResetPassRequest, token: String) {
        viewModelScope.launch {
            userUseCase.resetPass(request, token).collect {
                _resetResult.value = it
            }
        }
    }

}