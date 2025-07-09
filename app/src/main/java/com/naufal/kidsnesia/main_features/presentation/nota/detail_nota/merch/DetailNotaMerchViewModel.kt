package com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.merch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaPembelianEvent
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaPembelianMerch
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailNotaMerchViewModel(
    private val eventUseCase: EventUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel(){

    private val _notaMerch = MutableStateFlow<Resource<NotaPembelianMerch>>(Resource.Loading())
    val notaMerch: StateFlow<Resource<NotaPembelianMerch>> = _notaMerch

    fun getDetailNotaMerch(id: String) {
        viewModelScope.launch {
            _notaMerch.value = Resource.Loading()
            try {
                val token = authLocalDataSource.getToken()
                val response = eventUseCase.getDetailNotaMerch("Bearer $token", id)
                val nota = response.notaPembelianMerch
                if (nota != null) {
                    _notaMerch.value = Resource.Success(nota)
                } else {
                    _notaMerch.value = Resource.Error("Nota tidak ditemukan")
                }
            } catch (e: Exception) {
                _notaMerch.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}