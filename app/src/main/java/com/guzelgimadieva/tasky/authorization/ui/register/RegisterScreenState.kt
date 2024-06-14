package com.guzelgimadieva.tasky.authorization.ui.register

data class RegisterScreenState (
    val username: String = "",
    val usernameValid: Boolean = false,
    val email: String = "",
    val emailValid: Boolean = false,
    val password: String = "",
    val passwordVisible: Boolean = false,
)