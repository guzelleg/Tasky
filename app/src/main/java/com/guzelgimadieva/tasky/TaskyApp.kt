package com.guzelgimadieva.tasky

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guzelgimadieva.tasky.agenda.ui.AgendaScreen
import com.guzelgimadieva.tasky.authorization.ui.login.LoginScreen
import com.guzelgimadieva.tasky.authorization.ui.register.RegisterScreen

@Composable
fun TaskyApp() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = hiltViewModel()
    val context = LocalContext.current

    val refreshToken = context.getSharedPreferences(
        context.getString(R.string.stored_refresh_token), Context.MODE_PRIVATE )
        .all[context.getString(R.string.stored_refresh_token)].toString()
    val userId = context.getSharedPreferences(
        context.getString(R.string.stored_user_id), Context.MODE_PRIVATE)
        .all[context.getString(R.string.stored_user_id)].toString()

    if (refreshToken.isNotEmpty() && userId.isNotEmpty()) {
        viewModel.getAccessToken(refreshToken, userId)
    }

    val startDestination = if (viewModel.isLoggedIn.collectAsState().value) {
        "agenda"
    } else {
        "login"
    }
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate("register")
                },
            )
        }
        composable("register") {
            RegisterScreen(
                onBackClick = {
                    navController.navigate("login")
                },
            )
        }
        composable("agenda") {
            AgendaScreen()
        }
    }
}