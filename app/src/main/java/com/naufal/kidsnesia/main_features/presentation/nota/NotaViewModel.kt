package com.naufal.kidsnesia.main_features.presentation.nota

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListNotaPembelianEventItem
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListNotaPembelianMerchItem
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotaViewModel(
    private val eventUseCase: EventUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _notaEvent = MutableStateFlow<Resource<List<ListNotaPembelianEventItem>>>(Resource.Loading())
    val notaEvent = _notaEvent.asStateFlow()

    private val _notaMerch = MutableStateFlow<Resource<List<ListNotaPembelianMerchItem>>>(Resource.Loading())
    val notaMerch = _notaMerch.asStateFlow()

    fun getListNotaEvent() {
        viewModelScope.launch {
            val token = authLocalDataSource.getToken().orEmpty()
            _notaEvent.value = Resource.Loading()
            try {
                val response = eventUseCase.getNotaEvent("Bearer $token")
                _notaEvent.value = Resource.Success(response.listNotaPembelianEvent?.filterNotNull().orEmpty())
            } catch (e: Exception) {
                _notaEvent.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun getListNotaMerch() {
        viewModelScope.launch {
            val token = authLocalDataSource.getToken().orEmpty()
            _notaMerch.value = Resource.Loading()
            try {
                val response = eventUseCase.getNotaMerch("Bearer $token")
                _notaMerch.value = Resource.Success(response.listNotaPembelianMerch?.filterNotNull().orEmpty())
            } catch (e: Exception) {
                _notaMerch.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}
