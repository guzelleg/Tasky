package com.guzelgimadieva.tasky.authorization.ui.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RegisterScreenViewModel @Inject constructor() : ViewModel() {
    private val _registerState = MutableStateFlow(RegisterScreenState())
    val registerState: StateFlow<RegisterScreenState> = _registerState.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.UsernameChanged -> {
                _registerState.update { it.copy(username = event.username) }
            }
            is RegisterEvent.EmailChanged -> {
                _registerState.update { it.copy(email = event.email) }
            }
            is RegisterEvent.PasswordChanged -> {
                _registerState.update {
                    it.copy(password = event.password) }
            }
            is RegisterEvent.PasswordVisibilityChanged -> {
                _registerState.update { it.copy(
                    passwordVisible = !it.passwordVisible) }
            }
            is RegisterEvent.RegisterClicked -> {} // TODO: Implement register logic
        }
    }
}