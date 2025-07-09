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
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.ResetPassResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.SendEmailResponse
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpRequest
import com.naufal.kidsnesia.auth.data.source.remote.response.VerifyOtpResponse
import com.naufal.kidsnesia.main_features.data.source.remote.response.PelangganResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.HttpException

class AuthRemoteDataSource(private val apiService: ApiService) {

    fun register(namaPelanggan: String, email: String, password: String, noHpPelanggan: String): Flow<ApiResponse<RegisterResponse>> {
        return flow {
            try {
                val request = RegisterRequest(namaPelanggan, email, password, noHpPelanggan)
                val response = apiService.register(request)
                emit(ApiResponse.Success(response))
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    val json = JSONObject(errorBody ?: "")
                    json.optString("message", "Terjadi kesalahan")
                } catch (ex: Exception) {
                    "Terjadi kesalahan"
                }

                emit(ApiResponse.Error(errorMessage))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.localizedMessage ?: "Terjadi kesalahan"))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun verifyOtp(request: OtpRequest, tokenVerifikasi: String): Flow<ApiResponse<OtpResponse>> = flow {
        try {
            val response = apiService.otp(request, "Bearer $tokenVerifikasi")
            emit(ApiResponse.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorMessage = try {
                val json = JSONObject(errorBody ?: "")
                json.optString("message", "Verifikasi OTP gagal")
            } catch (ex: Exception) {
                "Verifikasi OTP gagal"
            }
            emit(ApiResponse.Error(errorMessage))  // <- tampilkan pesan dari server
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Verifikasi OTP gagal"))
        }
    }.flowOn(Dispatchers.IO)

    fun resendOtp(tokenVerifikasi: String): Flow<ApiResponse<ResendOtpResponse>> = flow {
        try {
            val response = apiService.resend("Bearer $tokenVerifikasi")
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Gagal mengirim ulang OTP"))
        }
    }.flowOn(Dispatchers.IO)

    fun sendEmail(request: SendEmailRequest): Flow<ApiResponse<SendEmailResponse>> = flow {
        try {
            val response = apiService.sendEmail(request)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Email yang di masukkan salah!"))
        }
    }.flowOn(Dispatchers.IO)

    fun verifyResetOtp(request: VerifyOtpRequest): Flow<ApiResponse<VerifyOtpResponse>> = flow {
        try {
            val response = apiService.verifyResetOtp(request)
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "OTP tidak valid"))
        }
    }.flowOn(Dispatchers.IO)

    fun resetPass(request: ResetPassRequest, tokenReset: String): Flow<ApiResponse<ResetPassResponse>> = flow {
        try {
            val response = apiService.resetPass(request, "Bearer $tokenReset")
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Password tidak boleh sama dengan yang sebelumnya"))
        }
    }.flowOn(Dispatchers.IO)

    fun login(email: String, password: String): Flow<ApiResponse<LoginResponse>> = flow {
        try {
            val response = apiService.login(LoginRequest(email, password))
            if (response.message.isNullOrEmpty()) {
                emit(ApiResponse.Empty)
            } else {
                emit(ApiResponse.Success(response))
            }
        }
        catch (e: Exception) {
            val message = if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                try {
                    val json = JSONObject(errorBody ?: "")
                    json.optString("message", "Terjadi kesalahan")
                } catch (ex: Exception) {
                    errorBody ?: "Terjadi kesalahan"
                }
            } else {
                e.localizedMessage ?: "Terjadi kesalahan"
            }

            emit(ApiResponse.Error(message))
        }
    }.flowOn(Dispatchers.IO)

    fun getCurrentPelanggan(): Flow<ApiResponse<PelangganResponse>> = flow {
        try {
            val response = apiService.getCurrentPelanggan() // Tidak perlu kirim token manual
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Error(e.localizedMessage ?: "Unknown Error"))
        }
    }.flowOn(Dispatchers.IO)


}