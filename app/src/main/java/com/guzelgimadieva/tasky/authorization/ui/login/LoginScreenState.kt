package com.guzelgimadieva.tasky.authorization.ui.login

data class LoginScreenState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false
)