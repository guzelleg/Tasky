package com.guzelgimadieva.tasky.authorization.ui.login

import androidx.core.app.NotificationCompat.MessagingStyle.Message

data class LoginScreenState(
    val email: String = "",
    val emailValid: Boolean = false,
    val password: String = "",
    val passwordVisible: Boolean = false,
    val errorMessage: String? = null,
)