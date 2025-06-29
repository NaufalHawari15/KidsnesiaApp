package com.naufal.kidsnesia.auth.data.source.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.naufal.kidsnesia.auth.data.source.remote.response.LoginResponse
import com.naufal.kidsnesia.auth.domain.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference(private val dataStore: DataStore<Preferences>) {

    private val EMAIL_KEY = stringPreferencesKey("email")
    private val TOKEN_KEY = stringPreferencesKey("token")
    private val IS_LOGIN_KEY = booleanPreferencesKey("is_login")
    private val MEMBERSHIP_STATUS_KEY = booleanPreferencesKey("is_member")


    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = user.isLogin
        }
        Log.d("SessionCheck", "Saving session for user: ${user.email}, token: ${user.token}")
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                email = preferences[EMAIL_KEY] ?: "",
                token = preferences[TOKEN_KEY] ?: "",
                isLogin = preferences[IS_LOGIN_KEY] ?: false
            )
        }
    }

    suspend fun saveMembershipStatus(isMember: Boolean) {
        dataStore.edit { preferences ->
            preferences[MEMBERSHIP_STATUS_KEY] = isMember
        }
    }

    fun getMembershipStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[MEMBERSHIP_STATUS_KEY] ?: false
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences.remove(EMAIL_KEY)
            preferences.remove(TOKEN_KEY)
            preferences.remove(IS_LOGIN_KEY)
        }
    }
}