package com.guzelgimadieva.tasky.authorization.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(): ViewModel() {
    private val _loginState = MutableStateFlow(LoginScreenState())
    val loginState: StateFlow<LoginScreenState> = _loginState.asStateFlow()

    private fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _loginState.value = _loginState.value.copy(email = event.email)
            }
            is LoginEvent.PasswordChanged -> {
                _loginState.value = _loginState.value.copy(password = event.password)
            }
            is LoginEvent.PasswordVisibilityChanged -> {
                _loginState.value = _loginState.value.copy(passwordVisible = event.isPasswordVisible)
            }
            is LoginEvent.Login -> {
                // TODO: Implement login logic
            }
        }
    }

    fun sendEvent(event: LoginEvent) {
        onEvent(event)
    }
}