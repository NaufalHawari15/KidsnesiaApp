package com.naufal.kidsnesia.main_features.presentation.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.EventResponse
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class EventViewModel(private val eventUseCase: EventUseCase) : ViewModel() {

    private val _eventList = MutableStateFlow<Resource<EventResponse>>(Resource.Loading())
    val eventList: StateFlow<Resource<EventResponse>> = _eventList.asStateFlow()

    fun getEvents() {
        viewModelScope.launch {
            eventUseCase.listEvent().collect { response ->
                _eventList.value = response
            }
        }
    }
}

