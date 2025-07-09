package com.naufal.kidsnesia.auth.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
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
import com.naufal.kidsnesia.main_features.data.source.remote.response.PelangganResponse
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun register(namaPelanggan: String, email: String, password: String, noHpPelanggan: String): Flow<Resource<RegisterResponse>>

    fun verifyOtp(request: OtpRequest, tokenVerifikasi: String): Flow<Resource<OtpResponse>>

    fun resendOtp(tokenVerifikasi: String): Flow<Resource<ResendOtpResponse>>

    fun sendEmail(request: SendEmailRequest): Flow<Resource<SendEmailResponse>>

    fun verifyResetOtp(request: VerifyOtpRequest): Flow<Resource<VerifyOtpResponse>>

    fun resetPass(request: ResetPassRequest, tokenReset: String): Flow<Resource<ResetPassResponse>>

    fun login(email: String, password: String): Flow<Resource<LoginResponse>>

    fun getCurrentPelanggan(): Flow<Resource<PelangganResponse>>

    fun getSession(): Flow<UserModel>

    suspend fun saveSession(user: UserModel)

    suspend fun logout()
}