package com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaPembelianEvent
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailNotaEventViewModel(
    private val eventUseCase: EventUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _notaEvent = MutableStateFlow<Resource<NotaPembelianEvent>>(Resource.Loading())
    val notaEvent: StateFlow<Resource<NotaPembelianEvent>> = _notaEvent

    fun getDetailNotaEvent(id: String) {
        viewModelScope.launch {
            _notaEvent.value = Resource.Loading()
            try {
                val token = authLocalDataSource.getToken()
                val response = eventUseCase.getDetailNotaEvent("Bearer $token", id)
                val nota = response.notaPembelianEvent
                if (nota != null) {
                    _notaEvent.value = Resource.Success(nota)
                } else {
                    _notaEvent.value = Resource.Error("Nota tidak ditemukan")
                }
            } catch (e: Exception) {
                _notaEvent.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

}