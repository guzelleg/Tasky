package com.guzelgimadieva.tasky.authorization.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guzelgimadieva.tasky.authorization.ui.utils.validateEmail
import com.guzelgimadieva.tasky.core.data.remote.model.RegisterRequest
import com.guzelgimadieva.tasky.core.network.TaskyServiceImpl
import com.guzelgimadieva.tasky.core.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor() : ViewModel() {
    private val _registerState = MutableStateFlow(RegisterScreenState())
    val registerState: StateFlow<RegisterScreenState> = _registerState.asStateFlow()
    private val service = TaskyServiceImpl()

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
                    it.copy(password = event.password) }
            }
            is RegisterEvent.PasswordVisibilityChanged -> {
                _registerState.update { it.copy(
                    passwordVisible = !it.passwordVisible) }
            }
            is RegisterEvent.RegisterClicked -> {
                viewModelScope.launch {
                    sendRegister()
                }
            }
        }
    }

    private fun validateUsername(name:String) {
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
            service.isLoggedIn = true
        }
    }
}