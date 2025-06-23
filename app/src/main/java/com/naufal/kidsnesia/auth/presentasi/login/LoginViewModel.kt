package com.naufal.kidsnesia.auth.presentasi.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.LoginResponse
import com.naufal.kidsnesia.auth.domain.model.UserModel
import com.naufal.kidsnesia.auth.domain.usecase.UserUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class LoginViewModel(private val userUseCase: UserUseCase) : ViewModel() {
    private val _loginResult = MutableLiveData<Resource<LoginResponse>>()
    val loginResult: LiveData<Resource<LoginResponse>> get() = _loginResult
    val loginState = MutableStateFlow<Resource<LoginResponse>>(Resource.Loading())

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun login(email: String, password: String) {
        viewModelScope.launch {
            userUseCase.login(email, password)
                .onStart {
                    _loginResult.postValue(Resource.Loading())
                    loginState.value = Resource.Loading()
                    delay(200)
                }
                .catch { e ->
                    _loginResult.postValue(Resource.Error(e.localizedMessage ?: "Unknown Error"))
                    loginState.value = Resource.Error(e.localizedMessage ?: "Unknown Error")
                }
                .collect { result ->
                    _loginResult.postValue(result)
                    loginState.value = result
                    if (result is Resource.Success) {
                        result.data?.loginResult?.let { loginResult ->
                            val user = UserModel(
                                email = loginResult.email ?: "",
                                token = loginResult.token ?: "", // ✅ Ambil token dengan benar
                                isLogin = true
                            )
                            saveSession(user)
                        }
                    }
                }

        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            Log.d("SessionCheck", "Saving session for user: ${user.email}, token: ${user.token}")
            userUseCase.saveSession(user)
        }
    }
}