package com.guzelgimadieva.tasky.core.network
import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.data.remote.model.LoginResponse
import com.guzelgimadieva.tasky.core.data.remote.model.RegisterRequest
import com.guzelgimadieva.tasky.BuildConfig
import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenRequest
import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenResponse
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
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okhttp3.Response
import kotlin.coroutines.cancellation.CancellationException

class TaskyServiceImpl: TaskyService {

    private var accessToken: String? = null
    var isLoggedIn = false

    fun invalidateCaches() {
        accessToken = null
        isLoggedIn = false
    }

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
    override suspend fun register(registerRequest: RegisterRequest): Result<Response, DataError.Network> {
        return safeCall<Response> {
            client.post(HttpRoutes.REGISTER){
                setBody(registerRequest)
            }
        }.also { isLoggedIn = it is Result.Success }
    }
    override suspend fun login(loginRequest: LoginRequest): Result<LoginResponse, Error> {
       return safeCall<LoginResponse> {
            client.post(HttpRoutes.LOGIN){
                setBody(loginRequest)
            }
        }.also { isLoggedIn = it is Result.Success }
    }

    override suspend fun accessToken(accessTokenRequest: AccessTokenRequest): Result<AccessTokenResponse, Error> {
        return safeCall<AccessTokenResponse> {
            client.post(HttpRoutes.ACCESS_TOKEN){
                setBody(accessTokenRequest)
            }
        }.also {
            accessToken = when(it){
                is Result.Success -> it.data.accessToken
                else -> null
            }
        }
    }

    private suspend inline fun <reified T> safeCall(execute: () -> HttpResponse): Result<T, DataError.Network> {
        val response = try {
            execute()
        } catch (e: UnresolvedAddressException) {
            e.printStackTrace()
            return Result.Error(DataError.Network.NO_INTERNET)
        } catch (e: SerializationException) {
            e.printStackTrace()
            return Result.Error(DataError.Network.SERIALIZATION)
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            e.printStackTrace()
            return Result.Error(DataError.Network.UNKNOWN)
        }
        return responseToResult(response)
    }

    suspend inline fun <reified T> responseToResult(
        response: HttpResponse,
    ): Result<T, DataError.Network> {
        return when (response.status.value) {
            in 200..299 -> Result.Success(response.body<T>())
            401 -> Result.Error(DataError.Network.UNAUTHORIZED)
            408 -> Result.Error(DataError.Network.REQUEST_TIMEOUT)
            409 -> Result.Error(DataError.Network.CONFLICT)
            413 -> Result.Error(DataError.Network.PAYLOAD_TOO_LARGE)
            429 -> Result.Error(DataError.Network.TOO_MANY_REQUESTS)
            in 500..599 -> Result.Error(DataError.Network.SERVER_ERROR)
            else -> Result.Error(DataError.Network.UNKNOWN)
        }
    }
}