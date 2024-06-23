package com.guzelgimadieva.tasky.core.data.local

import com.guzelgimadieva.tasky.core.data.remote.model.local.AuthenticatedUser

interface UserPreferences {
    fun saveAuthenticatedUser(user: AuthenticatedUser)
    fun getAuthenticatedUser(): AuthenticatedUser?
    fun clearPreferences()

    companion object {
        const val TOKEN = "TOKEN"
        const val REFRESH_TOKEN = "REFRESH_TOKEN"
        const val USER_ID = "USER_ID"
        const val EMAIL = "EMAIL"
    }
}