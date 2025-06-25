package com.naufal.kidsnesia.purchase.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartMerchResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartResponse
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _cartEvent = MutableStateFlow<Resource<ListCartResponse>>(Resource.Loading())
    val cartEvent = _cartEvent.asStateFlow()

    private val _cartMerch = MutableStateFlow<Resource<ListCartMerchResponse>>(Resource.Loading())
    val cartMerch = _cartMerch.asStateFlow()

    fun getListCartEvent() {
        viewModelScope.launch {
            val token = authLocalDataSource.getToken().orEmpty()
            _cartEvent.value = Resource.Loading()
            try {
                val response = purchaseUseCase.getListCart("Bearer $token")
                _cartEvent.value = Resource.Success(response)
            } catch (e: Exception) {
                _cartEvent.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun getListCartMerch() {
        viewModelScope.launch {
            val token = authLocalDataSource.getToken().orEmpty()
            _cartMerch.value = Resource.Loading()
            try {
                val response = purchaseUseCase.getListMerchCart("Bearer $token")
                _cartMerch.value = Resource.Success(response)
            } catch (e: Exception) {
                _cartMerch.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}