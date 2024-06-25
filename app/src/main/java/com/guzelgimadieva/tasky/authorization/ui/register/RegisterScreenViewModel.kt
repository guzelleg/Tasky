package com.guzelgimadieva.tasky.authorization.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guzelgimadieva.tasky.authorization.ui.utils.validateEmail
import com.guzelgimadieva.tasky.core.data.local.UserPreferences
import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.data.remote.model.RegisterRequest
import com.guzelgimadieva.tasky.core.data.remote.model.local.AuthenticatedUser
import com.guzelgimadieva.tasky.core.network.TaskyServiceImpl
import com.guzelgimadieva.tasky.core.network.onSuccess
import com.guzelgimadieva.tasky.core.util.Auth
import com.guzelgimadieva.tasky.core.util.AuthEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor(
    private val service: TaskyServiceImpl,
    private val userPreferences: UserPreferences,
    private val authEvent: Auth,
) : ViewModel() {
    private val _registerState = MutableStateFlow(RegisterScreenState())
    val registerState: StateFlow<RegisterScreenState> = _registerState.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameChanged -> {
                _registerState.update { it.copy(username = event.username) }
                validateUsername(event.username)
            }
            is RegisterEvent.EmailChanged -> {
                _registerState.update { it.copy(email = event.email) }
                val emailValid = validateEmail(event.email)
                if (emailValid) {
                    _registerState.update { it.copy(emailValid = true) }
                } else {
                    _registerState.update { it.copy(emailValid = false) }
                }
            }
            is RegisterEvent.PasswordChanged -> {
                _registerState.update {
                    it.copy(password = event.password)
                }
            }
            is RegisterEvent.PasswordVisibilityChanged -> {
                _registerState.update {
                    it.copy(
                        passwordVisible = !it.passwordVisible
                    )
                }
            }
            is RegisterEvent.RegisterClicked -> {
                viewModelScope.launch {
                    sendRegister()
                }
            }
        }
    }

    private fun validateUsername(name: String) {
        if (name.length < 3) {
            _registerState.update { it.copy(usernameValid = false) }
        } else {
            _registerState.update { it.copy(usernameValid = true) }
        }
    }

    private suspend fun sendRegister() {
        service.register(
            RegisterRequest(
                _registerState.value.username,
                _registerState.value.email,
                _registerState.value.password
            )
        ).onSuccess {
            service.login(
                LoginRequest(
                    _registerState.value.email,
                    _registerState.value.password,
                )
            ).onSuccess { user ->
                authEvent.sendEvent(AuthEvent.LogIn)
                userPreferences.saveAuthenticatedUser(
                    AuthenticatedUser(
                        user.userId,
                        _registerState.value.email,
                        user.accessToken,
                        user.refreshToken,
                    )
                )
            }
        }
    }
}