package com.guzelgimadieva.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guzelgimadieva.tasky.core.data.local.UserPreferences
import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenRequest
import com.guzelgimadieva.tasky.core.network.TaskyServiceImpl
import com.guzelgimadieva.tasky.core.network.onError
import com.guzelgimadieva.tasky.core.network.onSuccess
import com.guzelgimadieva.tasky.core.util.Auth
import com.guzelgimadieva.tasky.core.util.AuthEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val service: TaskyServiceImpl,
    private val preferences: UserPreferences,
    private val authEvent: Auth,
) : ViewModel() {
    private var _state = MutableStateFlow(
        AppState(
            isLoggedIn = preferences.getAuthenticatedUser() != null,
        )
    )
    val state = _state.asStateFlow()

    init {
        authEvent.authFlow.onEach { event ->
            when (event) {
                AuthEvent.LogIn -> {
                    _state.value = _state.value.copy(isLoggedIn = true)
                }
                AuthEvent.LogOut -> {
                    _state.value = _state.value.copy(isLoggedIn = false)
                }
            }
        }.launchIn(viewModelScope)
        viewModelScope.launch {
            if (_state.value.isLoggedIn) {
                refreshToken()
            } else {
                _state.value = _state.value.copy(isLoading = false)
            }
        }
    }

    private suspend fun refreshToken() {
        service.accessToken(
            AccessTokenRequest(
                preferences.getAuthenticatedUser()?.refreshToken ?: "",
                preferences.getAuthenticatedUser()?.userId ?: "",
            )
        )
            .onSuccess {
                _state.value = _state.value.copy(isLoading = false)

            }
            .onError {
                _state.value = _state.value.copy(isLoading = false, isLoggedIn = false)
            }
    }


}
