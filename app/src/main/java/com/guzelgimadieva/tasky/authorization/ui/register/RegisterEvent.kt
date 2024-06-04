package com.guzelgimadieva.tasky.authorization.ui.register

sealed interface RegisterEvent {

    data class UsernameChanged(
        val username: String
    ) : RegisterEvent
    data class EmailChanged(
        val email: String
    ) : RegisterEvent

    data class PasswordChanged(
        val password: String
    ) : RegisterEvent
    data object PasswordVisibilityChanged : RegisterEvent

    data object ReisterClicked : RegisterEvent

}