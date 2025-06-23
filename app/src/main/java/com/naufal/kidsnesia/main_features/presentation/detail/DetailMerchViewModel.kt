package com.naufal.kidsnesia.main_features.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailProductResponse
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailMerchViewModel(private val eventUseCase: EventUseCase) : ViewModel() {
    private val _productDetail = MutableStateFlow<Resource<DetailProductResponse>>(Resource.Loading())
    val productDetail: StateFlow<Resource<DetailProductResponse>> = _productDetail.asStateFlow()

    fun getDetailProduct(idMerch: String) {
        viewModelScope.launch {
            eventUseCase.detailProduct(idMerch).collect { response ->
                _productDetail.value = response
            }
        }
    }
}