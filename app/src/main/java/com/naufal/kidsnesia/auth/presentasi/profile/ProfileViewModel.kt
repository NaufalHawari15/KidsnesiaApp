package com.naufal.kidsnesia.auth.presentasi.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.domain.model.UserModel
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import com.naufal.kidsnesia.main_features.data.source.remote.response.PelangganResponse
import kotlinx.coroutines.launch

class ProfileViewModel(private val userUseCase: UserUseCase) : ViewModel() {

    private val _profile = MutableLiveData<Resource<PelangganResponse>>()
    val profile: LiveData<Resource<PelangganResponse>> = _profile

    init {
        getProfile()
    }

    fun getSession(): LiveData<UserModel> {
        return userUseCase.getSession().asLiveData()
    }

    fun getProfile() {
        viewModelScope.launch {
            _profile.value = Resource.Loading()
            userUseCase.getCurrentPelanggan().collect { result ->
                _profile.value = result
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userUseCase.logout()
        }
    }
}

