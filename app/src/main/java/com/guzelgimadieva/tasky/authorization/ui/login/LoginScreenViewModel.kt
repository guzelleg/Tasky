package com.guzelgimadieva.tasky.authorization.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guzelgimadieva.tasky.authorization.ui.utils.validateEmail
import com.guzelgimadieva.tasky.core.data.local.UserPreferences
import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.data.remote.model.local.AuthenticatedUser
import com.guzelgimadieva.tasky.core.network.DataError
import com.guzelgimadieva.tasky.core.network.DataError.Companion.toUiText
import com.guzelgimadieva.tasky.core.network.TaskyServiceImpl
import com.guzelgimadieva.tasky.core.network.onError
import com.guzelgimadieva.tasky.core.network.onSuccess
import com.guzelgimadieva.tasky.core.util.Auth
import com.guzelgimadieva.tasky.core.util.AuthEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val service: TaskyServiceImpl,
    private val userPreferences: UserPreferences,
    private val authEvent: Auth,
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginScreenState())
    val loginState: StateFlow<LoginScreenState> = _loginState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _loginState.update { it.copy(email = event.email) }
                if (validateEmail(event.email)) {
                    _loginState.update { it.copy(emailValid = true) }
                } else {
                    _loginState.update { it.copy(emailValid = false) }
                }
            }
            is LoginEvent.PasswordChanged -> {
                _loginState.update { it.copy(password = event.password) }
            }
            is LoginEvent.PasswordVisibilityChanged -> {
                _loginState.update { it.copy(passwordVisible = !_loginState.value.passwordVisible) }
            }
            is LoginEvent.Login -> {
                viewModelScope.launch {
                    sendLogin()
                }
            }
        }
    }

    private suspend fun sendLogin() {
        service.login(
            LoginRequest(
                _loginState.value.email,
                _loginState.value.password
            )
        ).onSuccess { user ->
            userPreferences.saveAuthenticatedUser(AuthenticatedUser(
                user.userId,
                _loginState.value.email,
                user.accessToken,
                user.refreshToken,
            ))
            authEvent.sendEvent(AuthEvent.LogIn)
        }.onError { error ->
            if (error is DataError) {
                _loginState.update { it.copy(errorMessage = error.toUiText()) }
            } else {
                // Handle other types of errors
            }
        }
    }
}