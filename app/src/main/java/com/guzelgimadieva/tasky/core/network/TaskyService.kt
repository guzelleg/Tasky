package com.guzelgimadieva.tasky.core.network

import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.data.remote.model.LoginResponse
import com.guzelgimadieva.tasky.core.data.remote.model.RegisterRequest
import io.ktor.client.statement.HttpResponse

interface TaskyService {
    suspend fun register(registerRequest: RegisterRequest)
    suspend fun login(loginRequest: LoginRequest): NetworkResponseState<LoginResponse>
}