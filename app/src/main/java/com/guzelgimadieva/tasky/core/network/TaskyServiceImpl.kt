package com.guzelgimadieva.tasky.core.network
import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.data.remote.model.LoginResponse
import com.guzelgimadieva.tasky.core.data.remote.model.RegisterRequest
import com.guzelgimadieva.tasky.BuildConfig
import com.guzelgimadieva.tasky.core.data.local.UserPreferences
import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenRequest
import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenResponse
import com.guzelgimadieva.tasky.core.data.remote.model.local.AuthenticatedUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
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
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class TaskyServiceImpl @Inject constructor(
    private val userPreferences: UserPreferences,
): TaskyService {

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

        install(Auth){
            bearer {
                loadTokens {
                    val info = userPreferences.getAuthenticatedUser()
                    BearerTokens(
                        accessToken = info?.accessToken ?: "",
                        refreshToken = info?.refreshToken ?: ""
                    )
                }
                refreshTokens {
                    val info = userPreferences.getAuthenticatedUser()
                    val response = safeCall<AccessTokenResponse> {
                        client.post(HttpRoutes.ACCESS_TOKEN) {
                            setBody(
                                AccessTokenRequest(
                                    refreshToken = info?.refreshToken ?: "",
                                    userId = info?.userId ?: ""
                                )
                            )
                        }
                    }
                    if(response is Result.Success) {
                        val newAuthInfo = AuthenticatedUser(
                            accessToken = response.data.accessToken,
                            refreshToken = info?.refreshToken ?: "",
                            userId = info?.userId ?: "",
                            email = info?.email ?: "",
                        )
                        userPreferences.saveAuthenticatedUser(newAuthInfo)

                        BearerTokens(
                            accessToken = newAuthInfo.accessToken,
                            refreshToken = newAuthInfo.refreshToken
                        )
                    } else {
                        BearerTokens(
                            accessToken = "",
                            refreshToken = ""
                        )
                    }
                }
            }
        }

    }
    override suspend fun register(registerRequest: RegisterRequest): Result<Response, DataError.Network> {
        return safeCall {
            client.post(HttpRoutes.REGISTER){
                setBody(registerRequest)
            }
        }
    }
    override suspend fun login(loginRequest: LoginRequest): Result<LoginResponse, Error> {
       val response = safeCall<LoginResponse> {
            client.post(HttpRoutes.LOGIN){
                setBody(loginRequest)
            }
        }

        if(response is Result.Success) userPreferences.saveAuthenticatedUser(
            response.data.toAuthenticatedUser())

        return response
    }

    override suspend fun accessToken(accessTokenRequest: AccessTokenRequest): Result<AccessTokenResponse, Error> {
        return safeCall<AccessTokenResponse> {
            client.post(HttpRoutes.ACCESS_TOKEN){
                setBody(accessTokenRequest)
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
