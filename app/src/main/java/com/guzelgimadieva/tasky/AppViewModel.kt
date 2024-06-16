package com.guzelgimadieva.tasky

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guzelgimadieva.tasky.core.data.remote.model.AccessTokenRequest
import com.guzelgimadieva.tasky.core.data.remote.model.LoginRequest
import com.guzelgimadieva.tasky.core.network.TaskyService
import com.guzelgimadieva.tasky.core.network.TaskyServiceImpl
import com.guzelgimadieva.tasky.core.network.onError
import com.guzelgimadieva.tasky.core.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppViewModel(
    private val service: TaskyServiceImpl
): ViewModel(){
    var isLoading: Boolean = true
        private set

    init {
        viewModelScope.launch {
        service.accessToken(
            AccessTokenRequest(
                "refreshToken",
                "userId"
            )
        ).onSuccess { response ->
            isLoading = false }
            .onError {
            }
        }
    }
}