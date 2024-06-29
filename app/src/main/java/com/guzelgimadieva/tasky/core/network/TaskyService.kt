package com.guzelgimadieva.tasky.core.network

import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenRequest
import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenResponse
import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.data.remote.model.LoginResponse
import com.guzelgimadieva.tasky.core.data.remote.model.RegisterRequest
import okhttp3.Response

interface TaskyService {
    suspend fun register(registerRequest: RegisterRequest): Result<Response, DataError.Network>
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse, Error>
    suspend fun accessToken(accessTokenRequest: AccessTokenRequest): Result<AccessTokenResponse, Error>
}