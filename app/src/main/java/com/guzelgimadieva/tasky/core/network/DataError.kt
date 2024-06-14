package com.guzelgimadieva.tasky.core.network

sealed interface DataError : Error {

    enum class Network : DataError {
        CONFLICT,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE, REQUEST_TIMEOUT,
        SERIALIZATION,
        SERVER_ERROR,
        TOO_MANY_REQUESTS,
        UNAUTHORIZED,
        UNKNOWN
    }

    enum class Local : DataError {
        DISK_FULL,
    }


    companion object {
        fun DataError.toUiText(): String {
            return when (this) {
                Network.CONFLICT -> "Conflict"
                Network.NO_INTERNET -> "No internet"
                Network.PAYLOAD_TOO_LARGE -> "Payload too large"
                Network.REQUEST_TIMEOUT -> "Request timeout"
                Network.SERIALIZATION -> "Serialization error"
                Network.SERVER_ERROR -> "Server error"
                Network.TOO_MANY_REQUESTS -> "Too many requests"
                Network.UNAUTHORIZED -> "Unauthorized"
                Network.UNKNOWN -> "Unknown error"
                Local.DISK_FULL -> "Disk full"
            }
        }
    }
}