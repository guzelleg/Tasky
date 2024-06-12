package com.guzelgimadieva.tasky.core.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val fullName: String,
    val email: String,
    val password: String,
)