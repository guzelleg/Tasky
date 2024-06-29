package com.guzelgimadieva.tasky

data class AppState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = true,
)