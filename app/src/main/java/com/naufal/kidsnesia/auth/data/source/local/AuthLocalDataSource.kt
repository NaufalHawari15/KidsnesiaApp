package com.naufal.kidsnesia.auth.data.source.local

import com.naufal.kidsnesia.auth.domain.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AuthLocalDataSource(private val userPreference: UserPreference) {
    suspend fun saveSession(user: UserModel) {
        withContext(Dispatchers.IO) { //  Pindahkan ke thread IO
            userPreference.saveSession(user)
        }
    }

    fun getSession(): Flow<UserModel> = userPreference.getSession()

    suspend fun logout() {
        userPreference.logout()
    }
}

