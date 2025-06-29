package com.naufal.kidsnesia.main_features.presentation.video

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DataItem
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class VideoViewModel(
    private val eventUseCase: EventUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _videoState = MutableStateFlow<Resource<List<DataItem>>>(Resource.Loading())
    val videoState: StateFlow<Resource<List<DataItem>>> = _videoState

    fun fetchVideoList() {
        viewModelScope.launch {
            _videoState.value = Resource.Loading()
            try {
                val token = authLocalDataSource.getToken()
                val response = eventUseCase.getListVideo("Bearer $token")
                val data = response.data?.filterNotNull() ?: emptyList()
                _videoState.value = Resource.Success(data)
            } catch (e: Exception) {
                _videoState.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    private val _membershipStatus = MutableStateFlow<Boolean?>(null)
    val membershipStatus: StateFlow<Boolean?> = _membershipStatus

    fun checkMembershipStatus() {
        viewModelScope.launch {
            try {
                val isCached = authLocalDataSource.getMembershipStatus().first()
                if (isCached) {
                    _membershipStatus.value = true
                    return@launch
                }

                val token = authLocalDataSource.getToken()
                val response = eventUseCase.getMembership("Bearer $token")
                val isActive = response.data?.statusMembership == "Aktif"

                _membershipStatus.value = isActive
                authLocalDataSource.saveMembershipStatus(isActive)
            } catch (e: Exception) {
                _membershipStatus.value = false
            }
        }
    }
}