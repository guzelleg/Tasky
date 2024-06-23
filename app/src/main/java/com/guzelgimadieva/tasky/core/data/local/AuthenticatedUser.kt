package com.guzelgimadieva.tasky.core.data.remote.model.local

data class AuthenticatedUser(
    val userId: String,
    val email: String,
    val accessToken: String,
    val refreshToken: String,
)
