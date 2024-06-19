package com.guzelgimadieva.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class AppViewModel @Inject constructor() : ViewModel() {
    private val service = TaskyServiceImpl()
    private var _isLoggedIn = MutableStateFlow(false)
    internal val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    init {
        service.invalidateCaches()
    }
    var isLoading: Boolean = true
        private set

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
            ).onSuccess { response ->
                isLoading = false
                _isLoggedIn.emit(true)
            }.onError {
                isLoading = false
            }
        }
    }
}