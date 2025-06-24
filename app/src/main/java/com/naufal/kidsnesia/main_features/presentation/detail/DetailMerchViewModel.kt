package com.naufal.kidsnesia.main_features.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import com.naufal.kidsnesia.purchase.data.source.remote.response.CartMerchRequest
import com.naufal.kidsnesia.purchase.data.source.remote.response.ItemsMerchItem
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailMerchViewModel(
    private val eventUseCase: EventUseCase,
    private val purchaseUseCase: PurchaseUseCase,
    private val authLocalDataSource: AuthLocalDataSource
    ) : ViewModel() {

    private val _productDetail = MutableStateFlow<Resource<DetailProductResponse>>(Resource.Loading())
    val productDetail: StateFlow<Resource<DetailProductResponse>> = _productDetail.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    fun getDetailProduct(idMerch: String) {
        viewModelScope.launch {
            eventUseCase.detailProduct(idMerch).collect { response ->
                _productDetail.value = response
            }
        }
    }

    fun createCartMerch(
        idMerch: Int,
        jumlahProduk: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _loadingState.value = true
            try {
                val token = authLocalDataSource.getToken()
                val request = CartMerchRequest(
                    itemsMerch = listOf(
                        ItemsMerchItem(idMerch = idMerch, jumlah = jumlahProduk)
                    )
                )
                val response = purchaseUseCase.createCartMerch("Bearer $token", request)
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