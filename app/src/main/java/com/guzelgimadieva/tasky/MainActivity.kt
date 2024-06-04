package com.guzelgimadieva.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.guzelgimadieva.tasky.authorization.ui.login.AppNavHost
import com.guzelgimadieva.tasky.authorization.ui.login.LoginScreen
import com.guzelgimadieva.tasky.core.theme.TaskyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TaskyTheme {
                AppNavHost(navController = rememberNavController())
            }
        }
    }
}