package com.naufal.kidsnesia.purchase.presentation.cart.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartEventDetail
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.CheckoutResponse
import com.naufal.kidsnesia.purchase.data.source.remote.response.ItemsEvent
import com.naufal.kidsnesia.purchase.data.source.remote.response.PembelianResponse
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailEventCartViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    private val _detailCart = MutableStateFlow<Resource<CartEventDetail>> (Resource.Loading())
    val detailCart: StateFlow<Resource<CartEventDetail>> get() = _detailCart

    fun getDetailCart(id: String) {
        viewModelScope.launch {
            _detailCart.value = Resource.Loading()
            val token = authLocalDataSource.getToken()
            try {
                val result = purchaseUseCase.getDetailCart("Bearer $token", id)
                val detail = result.cartEventDetail
                if (detail != null) {
                    _detailCart.value = Resource.Success(detail)
                } else {
                    _detailCart.value = Resource.Error("Detail keranjang kosong")
                }
            } catch (e: Exception) {
                _detailCart.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    private val _checkoutResult = MutableStateFlow<Resource<CheckoutResponse>>(Resource.Loading())
    val checkoutResult: StateFlow<Resource<CheckoutResponse>> get() = _checkoutResult

    fun createCheckout(idPembelianEvent: String, items: List<ItemsEvent>) {
        viewModelScope.launch {
            _checkoutResult.value = Resource.Loading()
            try {
                val token = authLocalDataSource.getToken()
                val request = CheckoutRequest(itemsEvent = items)
                val response = purchaseUseCase.createCheckout("Bearer $token", idPembelianEvent, request)
                _checkoutResult.value = Resource.Success(response)
            } catch (e: Exception) {
                _checkoutResult.value = Resource.Error(e.message ?: "Checkout gagal")
            }
        }
    }
}