package com.naufal.kidsnesia.auth.data.source.local

import com.naufal.kidsnesia.auth.domain.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class AuthLocalDataSource(private val userPreference: UserPreference) {
    suspend fun saveSession(user: UserModel) {
        withContext(Dispatchers.IO) { //  Pindahkan ke thread IO
            userPreference.saveSession(user)
        }
    }

    fun getSession(): Flow<UserModel> = userPreference.getSession()

    suspend fun getToken(): String {
        return getSession().first().token
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun saveMembershipStatus(isMember: Boolean) {
        userPreference.saveMembershipStatus(isMember)
    }

    fun getMembershipStatus(): Flow<Boolean> {
        return userPreference.getMembershipStatus()
    }
}