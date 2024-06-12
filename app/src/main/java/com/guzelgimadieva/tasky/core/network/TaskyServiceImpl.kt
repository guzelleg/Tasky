package com.guzelgimadieva.tasky.core.network
import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.data.remote.model.LoginResponse
import com.guzelgimadieva.tasky.core.data.remote.model.RegisterRequest
import com.guzelgimadieva.tasky.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class TaskyServiceImpl: TaskyService {

    private val client = HttpClient(OkHttp){
        install(Logging) {
            logger = Logger.SIMPLE
        }
        defaultRequest {
            header("x-api-key", BuildConfig.API_KEY)
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }

    }
    override suspend fun register(registerRequest: RegisterRequest) {
        safeRequest {
            client.post(HttpRoutes.REGISTER) {
                setBody(registerRequest)
            }
        }
    }

    override suspend fun login(loginRequest: LoginRequest): NetworkResponseState<LoginResponse> {
       return safeRequest {
            client.post(HttpRoutes.LOGIN){
                setBody(loginRequest)
            }.body<LoginResponse>()
        }
    }

    private inline fun <T> safeRequest(request: () -> T): NetworkResponseState<T> {
        return try {
            NetworkResponseState.Success(data = request())
        } catch (e: Exception) {
            NetworkResponseState.Error(exception = e)
        }
    }
}