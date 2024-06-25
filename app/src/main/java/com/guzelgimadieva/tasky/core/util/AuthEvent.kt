package com.guzelgimadieva.tasky.core.util

sealed interface AuthEvent {
    object LogOut: AuthEvent
    object LogIn: AuthEvent
}