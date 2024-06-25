package com.guzelgimadieva.tasky.core.data.remote.model

import com.guzelgimadieva.tasky.core.data.remote.model.local.AuthenticatedUser
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String,
    val accessTokenExpirationTimestamp: Long,
    val fullName: String,
    val refreshToken: String,
    val userId: String,
) {
    fun toAuthenticatedUser(): AuthenticatedUser {
        return AuthenticatedUser(
            userId = userId,
            email = fullName,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AccessTokenRequest(
    val refreshToken: String,
    val userId: String,
)

@Serializable
data class AccessTokenResponse(
    val accessToken: String,
    val expirationTimestamp: Long,
)



