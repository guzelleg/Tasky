package com.guzelgimadieva.tasky.core.network

sealed interface NetworkResponseState<T> {

    data class Success<T>(val data: T) : NetworkResponseState<T>
    data class Error<T>(val exception: Exception) : NetworkResponseState<T>
    fun onSuccess(block: (T) -> Unit): NetworkResponseState<T> {
        if (this is Success) {
            block(data)
        }
        return this
    }

    fun onError(block: (Throwable) -> Unit): NetworkResponseState<T> {
        if (this is Error) {
            block(exception)
        }
        return this
    }
}