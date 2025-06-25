package com.naufal.kidsnesia.purchase.presentation.cart.merch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.purchase.data.source.remote.response.ItemMerchCart
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailMerchCartViewModel(
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel(){

    private val _detailMerchCart = MutableStateFlow<Resource<ItemMerchCart>> (Resource.Loading())
    val detailMerchCart: StateFlow<Resource<ItemMerchCart>> get() = _detailMerchCart

    fun getDetailCartMerch(id: String) {
        viewModelScope.launch {
            _detailMerchCart.value = Resource.Loading()
            val token = authLocalDataSource.getToken()
            try {
                val result = purchaseUseCase.getDetailCartMerch("Bearer $token", id)
                val detail = result.itemMerchCart
                if (detail != null) {
                    _detailMerchCart.value = Resource.Success(detail)
                } else {
                    _detailMerchCart.value = Resource.Error("Detail keranjang kosong")
                }
            } catch (e: Exception) {
                _detailMerchCart.value = Resource.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }
}