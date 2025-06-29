package com.naufal.kidsnesia.main_features.presentation.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.auth.domain.model.UserModel
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import com.naufal.kidsnesia.main_features.data.source.remote.response.CurrentMembershipResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.ProductResponse
import com.naufal.kidsnesia.main_features.domain.usecase.EventUseCase

class DashboardViewModel(
    private val userUseCase: UserUseCase,
    private val eventUseCase: EventUseCase,
    private val authLocalDataSource: AuthLocalDataSource
) : ViewModel() {

    fun getSession(): LiveData<UserModel> = userUseCase.getSession().asLiveData()

    fun listProduct(): LiveData<Resource<ProductResponse>> {
        return eventUseCase.listProduct().asLiveData()
    }

    // Tambahan:
    suspend fun checkMembership(token: String): CurrentMembershipResponse {
        return eventUseCase.getMembership(token)
    }

    suspend fun getToken(): String {
        return authLocalDataSource.getToken()
    }

    suspend fun saveMembershipStatus(isActive: Boolean) {
        authLocalDataSource.saveMembershipStatus(isActive)
    }
}