package com.guzelgimadieva.tasky.core.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.guzelgimadieva.tasky.core.data.remote.model.local.AuthenticatedUser

class UserPreferencesImpl(
    private val sharedPref: SharedPreferences
) : UserPreferences {
    override fun saveAuthenticatedUser(user: AuthenticatedUser) {
        sharedPref.edit {
            putString(UserPreferences.USER_ID, user.userId)
            putString(UserPreferences.EMAIL, user.email)
            putString(UserPreferences.TOKEN, user.accessToken)
            putString(UserPreferences.REFRESH_TOKEN, user.refreshToken)
            .apply()
        }
    }

    override fun getAuthenticatedUser(): AuthenticatedUser? {
        val userId = sharedPref.getString(UserPreferences.USER_ID, null)
        val email = sharedPref.getString(UserPreferences.EMAIL, null)
        val accessToken = sharedPref.getString(UserPreferences.TOKEN, null)
        val refreshToken = sharedPref.getString(UserPreferences.REFRESH_TOKEN, null)

        if (userId != null && email != null && accessToken != null && refreshToken != null) return AuthenticatedUser(
            userId, email, accessToken, refreshToken
        )
        return null
    }

    override fun clearPreferences() {
        sharedPref.edit {
            clear().apply()
        }
    }
}