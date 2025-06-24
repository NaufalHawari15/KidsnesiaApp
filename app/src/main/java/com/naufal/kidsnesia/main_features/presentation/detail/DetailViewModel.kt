package com.naufal.kidsnesia.main_features.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailEventResponse
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.ItemsEventItem
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    private val eventUseCase: EventUseCase,
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _eventDetail = MutableStateFlow<Resource<DetailEventResponse>>(Resource.Loading())
    val eventDetail: StateFlow<Resource<DetailEventResponse>> = _eventDetail.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    fun getDetailEvents(id: String) {
        viewModelScope.launch {
            eventUseCase.detailEvent(id).collect {
                _eventDetail.value = it
            }
        }
    }

    fun createCart(
        idEvent: Int,
        jumlahTiket: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                val token = authLocalDataSource.getToken()
                val request = CartRequest(
                    itemsEvent = listOf(
                        ItemsEventItem(idEvent = idEvent, jumlahTiket = jumlahTiket)
                    )
                )
                val response = purchaseUseCase.createCart("Bearer $token", request)
                if (response.error == false) {
                    onSuccess()
                } else {
                    onError(response.message ?: "Terjadi kesalahan")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Gagal membuat cart")
            } finally {
                _loadingState.value = false
            }
        }
    }
}
