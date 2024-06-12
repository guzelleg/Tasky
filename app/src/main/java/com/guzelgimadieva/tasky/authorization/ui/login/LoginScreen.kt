@file:Suppress("PreviewMustBeTopLevelFunction")

package com.guzelgimadieva.tasky.authorization.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.guzelgimadieva.tasky.R
import com.guzelgimadieva.tasky.authorization.ui.components.InputTextField
import com.guzelgimadieva.tasky.authorization.ui.register.RegisterScreen
import com.guzelgimadieva.tasky.core.theme.TaskyAppGreen
import com.guzelgimadieva.tasky.core.theme.TaskyAppLightGray
import com.guzelgimadieva.tasky.core.theme.TaskyAppPurple


@Composable
fun AuthorizationScreen(
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AuthScreen.LOGIN.name,
    ) {
        composable(AuthScreen.LOGIN.name) {
            LoginScreen(
                onRegisterClick = {
                    navController.navigate(AuthScreen.REGISTER.name)
                }
            )
        }
        composable(AuthScreen.REGISTER.name) {
            RegisterScreen(
                onBackClick = {
                    navController.navigate(AuthScreen.LOGIN.name)
                }
            )
        }
    }
}

@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
) {
    val viewModel: LoginScreenViewModel = hiltViewModel()
    val loginState by viewModel.loginState.collectAsState()
    LoginScreenContent(
        onAction = viewModel::onEvent,
        loginState = loginState,
        onRegisterClick = onRegisterClick,
    )
}

@Composable
fun LoginScreenContent(
    onAction: (LoginEvent) -> Unit,
    loginState: LoginScreenState,
    onRegisterClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .windowInsetsPadding(WindowInsets.statusBars),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.welcome_screen_title),
            style = typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .padding(
                    vertical = 64.dp,
                )
        )

        Card(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(
                topStart = 30.dp,
                topEnd = 30.dp,
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                InputTextField(
                    value = loginState.email,
                    onValueChange = { onAction(LoginEvent.EmailChanged(it)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.register_screen_email_input),
                            style = TextStyle(color = Color.Black.copy(alpha = 0.5f))
                        )
                    },
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    trailingIcon = {
                        if (loginState.emailValid)
                            Icon(imageVector = Icons.Filled.Check,
                                tint = TaskyAppGreen,
                                contentDescription = null)
                    },
                )

                InputTextField(
                    value = loginState.password,
                    onValueChange = { onAction(LoginEvent.PasswordChanged(it)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.register_screen_password_input),
                            style = TextStyle(color = Color.Black.copy(alpha = 0.5f))
                        )
                    },
                    visualTransformation = if (loginState.passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (loginState.passwordVisible) Icons.Filled.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { onAction(LoginEvent.PasswordVisibilityChanged) }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onAction(LoginEvent.Login)},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_screen_login_button).uppercase(),
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                val annotatedString = getRegisterText()
                ClickableText(
                    text = annotatedString,
                    style = typography.bodyMedium.copy(
                        fontWeight = FontWeight.W500,
                        color = TaskyAppLightGray
                    ),
                    modifier = Modifier.padding(bottom = 30.dp),
                    onClick = { offset ->
                        annotatedString.getStringAnnotations(
                            tag = "Clickable",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                onRegisterClick()
                            }
                    }
                )
            }
        }
    }
}

@Composable
fun getRegisterText(): AnnotatedString {
    val text = stringResource(id = R.string.welcome_screen_register_text)
    val clickableText = stringResource(id = R.string.welcome_screen_register_link)
    return buildAnnotatedString {
        append(text)
        append(" ")
        withStyle(style = SpanStyle(color = TaskyAppPurple)) {
            pushStringAnnotation(tag = "Clickable", annotation = clickableText)
            append(clickableText)
            pop()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        onAction = {},
        loginState = LoginScreenState(),
        onRegisterClick = {},
    )
}

enum class AuthScreen {
    LOGIN,
    REGISTER
}
