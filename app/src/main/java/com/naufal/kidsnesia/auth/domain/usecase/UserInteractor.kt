package com.naufal.kidsnesia.auth.domain.usecase

import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.data.source.remote.response.OtpRequest
import com.naufal.kidsnesia.auth.domain.model.UserModel
import com.naufal.kidsnesia.auth.domain.repository.IUserRepository
import com.naufal.kidsnesia.main_features.data.source.remote.response.PelangganResponse
import kotlinx.coroutines.flow.Flow

class UserInteractor(private val userRepository: IUserRepository) : UserUseCase {

    override fun register(
        namaPelanggan: String,
        email: String,
        password: String,
        noHpPelanggan: String
    ) = userRepository.register(namaPelanggan, email, password, noHpPelanggan)

    override fun verifyOtp(request: OtpRequest, tokenVerifikasi: String) =
        userRepository.verifyOtp(request, tokenVerifikasi)

    override fun resendOtp(tokenVerifikasi: String) =
        userRepository.resendOtp(tokenVerifikasi)

    override fun login(
        email: String,
        password: String
    ) = userRepository.login(email, password)

    override fun getCurrentPelanggan(): Flow<Resource<PelangganResponse>> = userRepository.getCurrentPelanggan()

    override fun getSession(): Flow<UserModel> = userRepository.getSession()

    override suspend fun saveSession(user: UserModel) {
        userRepository.saveSession(user)
    }

    override suspend fun logout() {
        userRepository.logout()
    }
}
