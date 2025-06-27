package com.naufal.kidsnesia.main_features.presentation.detail.detail_video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailVideo
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailVideoViewModel(
    private val eventUseCase: EventUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _detailVideoState = MutableStateFlow<Resource<DetailVideo>>(Resource.Loading())
    val detailVideoState: StateFlow<Resource<DetailVideo>> = _detailVideoState

    fun fetchDetailVideo(idVideo: String) {
        viewModelScope.launch {
            _detailVideoState.value = Resource.Loading()
            try {
                val token = authLocalDataSource.getToken()
                val response = eventUseCase.getDetailVideo("Bearer $token", idVideo)
                response.detailVideo?.let {
                    _detailVideoState.value = Resource.Success(it)
                } ?: run {
                    _detailVideoState.value = Resource.Error("Data tidak ditemukan")
                }
            } catch (e: Exception) {
                _detailVideoState.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}
