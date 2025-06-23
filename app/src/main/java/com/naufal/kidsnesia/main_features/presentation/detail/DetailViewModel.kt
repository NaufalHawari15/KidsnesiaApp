package com.naufal.kidsnesia.main_features.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEventResponse
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel (private val eventUseCase: EventUseCase) : ViewModel() {

    private val _eventDetail = MutableStateFlow<Resource<DetailEventResponse>>(Resource.Loading())
    val eventDetail: StateFlow<Resource<DetailEventResponse>> = _eventDetail.asStateFlow()

    fun getDetailEvents(id: String) {
        viewModelScope.launch {
            eventUseCase.detailEvent(id).collect { response ->
                _eventDetail.value = response
            }
        }
    }
}