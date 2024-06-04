package com.guzelgimadieva.tasky.authorization.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {
    private val _loginState = MutableStateFlow(LoginScreenState())
    val loginState: StateFlow<LoginScreenState> = _loginState.asStateFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _loginState.update { _loginState.value.copy(email = event.email) }
            }
            is LoginEvent.PasswordChanged -> {
                _loginState.update { _loginState.value.copy(password = event.password) }
            }
            is LoginEvent.PasswordVisibilityChanged -> {
                _loginState.update { _loginState.value.copy(passwordVisible = !_loginState.value.passwordVisible) }
            }
            is LoginEvent.Login -> {
                // TODO: Implement login logic
            }
        }
    }
}