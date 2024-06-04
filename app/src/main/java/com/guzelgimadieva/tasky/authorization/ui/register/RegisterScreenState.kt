package com.guzelgimadieva.tasky.authorization.ui.register

data class RegisterScreenState (
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
)