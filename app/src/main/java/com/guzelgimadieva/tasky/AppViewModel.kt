package com.guzelgimadieva.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guzelgimadieva.tasky.core.data.local.UserPreferences
import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenRequest
import com.guzelgimadieva.tasky.core.network.TaskyServiceImpl
import com.guzelgimadieva.tasky.core.network.onError
import com.guzelgimadieva.tasky.core.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val service: TaskyServiceImpl,
    private val userPreferences: UserPreferences,
) : ViewModel() {
    private var _isLoggedIn = MutableStateFlow(false)
    internal val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        service.invalidateCaches()
    }
    private var isLoading: Boolean = true

    fun getAccessToken(
        refreshToken: String,
        userId: String
    ) {
        viewModelScope.launch {
            service.accessToken(
                AccessTokenRequest(
                    refreshToken,
                    userId,
                )
            ).onSuccess {
                isLoading = false
                service.isLoggedIn = true
                _isLoggedIn.emit(true)
            }.onError {
                isLoading = false
            }
        }
    }
}