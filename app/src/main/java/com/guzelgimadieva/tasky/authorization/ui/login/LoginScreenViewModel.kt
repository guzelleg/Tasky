package com.guzelgimadieva.tasky.authorization.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guzelgimadieva.tasky.authorization.ui.utils.validateEmail
import com.guzelgimadieva.tasky.core.network.TaskyService
import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.network.TaskyServiceImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {
    private val _loginState = MutableStateFlow(LoginScreenState())
    val loginState: StateFlow<LoginScreenState> = _loginState.asStateFlow()
    private val service = TaskyServiceImpl()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                _loginState.update { _loginState.value.copy(email = event.email) }
                if (validateEmail(event.email)) {
                    _loginState.update { _loginState.value.copy(emailValid = true) }
                } else {
                    _loginState.update { _loginState.value.copy(emailValid = false) }
                }
            }
            is LoginEvent.PasswordChanged -> {
                _loginState.update { _loginState.value.copy(password = event.password) }
            }
            is LoginEvent.PasswordVisibilityChanged -> {
                _loginState.update { _loginState.value.copy(passwordVisible = !_loginState.value.passwordVisible) }
            }
            is LoginEvent.Login -> {
                viewModelScope.launch {
                    sendLogin()
                    //TODO use response and check if token is null
                }
            }
        }
    }

    private suspend fun sendLogin()  {
        service.login(
            LoginRequest(
                _loginState.value.email,
                _loginState.value.password
            )
        ).onSuccess {
            //TODO handle success
        }.onError {
            println("Error: $it")
        }
    }
}