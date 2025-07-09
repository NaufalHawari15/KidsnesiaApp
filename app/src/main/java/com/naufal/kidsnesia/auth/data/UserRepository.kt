package com.naufal.kidsnesia.auth.data

import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.auth.data.source.remote.AuthRemoteDataSource
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.LoginResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.ResendOtpResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpResponse
import com.naufal.kidsnesia.auth.domain.model.UserModel
import com.naufal.kidsnesia.auth.domain.repository.IUserRepository
import com.naufal.kidsnesia.main_features.data.source.remote.response.PelangganResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val authLocalDataSource: AuthLocalDataSource
) : IUserRepository {
    override fun register(namaPelanggan: String, email: String, password: String, noHpPelanggan: String): Flow<Resource<RegisterResponse>> = flow {
        emit(Resource.Loading())
        authRemoteDataSource.register(namaPelanggan, email, password, noHpPelanggan).collect { apiResponse ->
            when (apiResponse) {
                is ApiResponse.Success -> emit(Resource.Success(apiResponse.data))
                is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty Response"))
            }
        }
    }

    override fun verifyOtp(request: OtpRequest, tokenVerifikasi: String): Flow<Resource<OtpResponse>> = flow {
        emit(Resource.Loading())
        authRemoteDataSource.verifyOtp(request, tokenVerifikasi).collect { response ->
            when (response) {
                is ApiResponse.Success -> emit(Resource.Success(response.data))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty response"))
            }
        }
    }

    override fun resendOtp(tokenVerifikasi: String): Flow<Resource<ResendOtpResponse>> = flow {
        emit(Resource.Loading())
        authRemoteDataSource.resendOtp(tokenVerifikasi).collect { response ->
            when (response) {
                is ApiResponse.Success -> emit(Resource.Success(response.data))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty response"))
            }
        }
    }

    override fun sendEmail(request: SendEmailRequest): Flow<Resource<SendEmailResponse>> = flow {
        emit(Resource.Loading())
        authRemoteDataSource.sendEmail(request).collect { response ->
            when(response) {
                is ApiResponse.Success -> emit(Resource.Success(response.data))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty response"))
            }
        }
    }

    override fun verifyResetOtp(request: VerifyOtpRequest): Flow<Resource<VerifyOtpResponse>> = flow {
        emit(Resource.Loading())
        authRemoteDataSource.verifyResetOtp(request).collect { response ->
            when(response) {
                is ApiResponse.Success -> emit(Resource.Success(response.data))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty response"))
            }
        }
    }

    override fun resetPass(request: ResetPassRequest, tokenReset: String): Flow<Resource<ResetPassResponse>> = flow {
        emit(Resource.Loading())
        authRemoteDataSource.resetPass(request, tokenReset).collect { response ->
            when(response) {
                is ApiResponse.Success -> emit(Resource.Success(response.data))
                is ApiResponse.Error -> emit(Resource.Error(response.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty response"))
            }
        }
    }

    override fun login(email: String, password: String): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.Loading())

        authRemoteDataSource.login(email, password).collect { apiResponse ->
            when (apiResponse) {
                is ApiResponse.Success -> {
                    apiResponse.data.loginResult?.let { loginResult ->
                        val user = UserModel(
                            email = loginResult.email ?: "",
                            token = loginResult.token ?: "", // Pastikan token tersimpan dengan benar
                            isLogin = true
                        )
                        authLocalDataSource.saveSession(user)
                    }
                    emit(Resource.Success(apiResponse.data))
                }
                is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                ApiResponse.Empty -> emit(Resource.Error("Empty response from server"))
            }
        }
    }

    override fun getCurrentPelanggan(): Flow<Resource<PelangganResponse>> = flow {
        emit(Resource.Loading())
        authLocalDataSource.getSession().collect { user ->
            if (user.token.isNotEmpty()) {
                authRemoteDataSource.getCurrentPelanggan().collect { apiResponse ->
                    when (apiResponse) {
                        is ApiResponse.Success -> emit(Resource.Success(apiResponse.data))
                        is ApiResponse.Error -> emit(Resource.Error(apiResponse.errorMessage))
                        ApiResponse.Empty -> emit(Resource.Error("Empty response from server"))
                    }
                }
            } else {
                emit(Resource.Error("User not logged in"))
            }
        }
    }

    override fun getSession(): Flow<UserModel> = authLocalDataSource.getSession()

    override suspend fun saveSession(user: UserModel) {
        authLocalDataSource.saveSession(user) // Kirim langsung objek UserModel
    }

    override suspend fun logout() {
        authLocalDataSource.logout()
    }
}