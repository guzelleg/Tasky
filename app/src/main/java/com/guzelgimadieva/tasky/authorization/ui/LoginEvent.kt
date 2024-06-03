package com.guzelgimadieva.tasky.authorization.ui

sealed interface LoginEvent {

    data class EmailChanged(
        val email: String
    ) : LoginEvent

    data class PasswordChanged(
       val password: String
    ) : LoginEvent

    data object PasswordVisibilityChanged : LoginEvent

    data object Login : LoginEvent
}
