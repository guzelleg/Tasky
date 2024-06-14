package com.guzelgimadieva.tasky.authorization.ui.utils

internal fun validateEmail(email: String) : Boolean =
    android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()