package com.guzelgimadieva.tasky.authorization.ui

sealed interface LoginEvent {

    data class EmailChanged(
        val email: String
    ) : LoginEvent

    data class PasswordChanged(
       val password: String
    ) : LoginEvent

    data class PasswordVisibilityChanged(
        val isPasswordVisible: Boolean
    ) : LoginEvent

    data class Login(val email: String, val password: String) : LoginEvent
}
