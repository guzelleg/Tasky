package com.guzelgimadieva.tasky

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.compose.rememberNavController
import com.guzelgimadieva.tasky.authorization.ui.login.AuthorizationScreen
import com.guzelgimadieva.tasky.core.theme.TaskyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        installSplashScreen().setKeepOnScreenCondition { appViewModel.isLoading }
        super.onCreate(savedInstanceState)
        setContent {
            TaskyTheme {
                AuthorizationScreen()
            }
        }
    }
}