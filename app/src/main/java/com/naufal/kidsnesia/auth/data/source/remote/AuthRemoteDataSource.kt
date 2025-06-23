package com.naufal.kidsnesia.auth.data.source.remote

import android.util.Log
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiResponse
import com.naufal.kidsnesia.auth.data.source.remote.network.ApiService
import com.naufal.kidsnesia.auth.data.source.remote.response.LoginRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.LoginResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.RegisterResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.ResendOtpResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.PelangganResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class AuthRemoteDataSource(private val apiService: ApiService) {

    suspend fun register(namaPelanggan: String, email: String, password: String, noHpPelanggan: String): Flow<ApiResponse<RegisterResponse>> {
        return flow {
            try {
                val request = RegisterRequest(namaPelanggan, email, password, noHpPelanggan)
                val response = apiService.register(request)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun verifyOtp(request: OtpRequest, tokenVerifikasi: String): Flow<ApiResponse<OtpResponse>> = flow {
        try {
            val response = apiService.otp(request, "Bearer $tokenVerifikasi")
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Verifikasi OTP gagal"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun resendOtp(tokenVerifikasi: String): Flow<ApiResponse<ResendOtpResponse>> = flow {
        try {
            val response = apiService.resend("Bearer $tokenVerifikasi")
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Gagal mengirim ulang OTP"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun login(email: String, password: String): Flow<ApiResponse<LoginResponse>> = flow {
        try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.message.isNullOrEmpty()) {
                emit(ApiResponse.Empty)
            } else {
                emit(ApiResponse.Success(response))
            }
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getCurrentPelanggan(): Flow<ApiResponse<PelangganResponse>> = flow {
        try {
            val response = apiService.getCurrentPelanggan() // Tidak perlu kirim token manual
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)
}