@file:Suppress("PreviewMustBeTopLevelFunction")

package com.guzelgimadieva.tasky.authorization.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.guzelgimadieva.tasky.R
import com.guzelgimadieva.tasky.core.theme.TaskyAppGray
import com.guzelgimadieva.tasky.core.theme.TaskyAppLightGray
import com.guzelgimadieva.tasky.core.theme.TaskyAppPurple

@Composable
fun LoginScreen() {
    val viewModel: LoginScreenViewModel = hiltViewModel()
    val loginState by viewModel.loginState.collectAsState()
    LoginScreenContent(
        onAction = viewModel::sendEvent,
        loginState = loginState
    )
}

@Composable
fun LoginScreenContent(
    onAction: (LoginEvent) -> Unit,
    loginState: LoginScreenState,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
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

                OutlinedTextField(
                    value = loginState.email,
                    onValueChange = {
                        onAction(LoginEvent.EmailChanged(it))
                    },
                    placeholder = { Text(stringResource(id = R.string.register_screen_email_input)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedContainerColor = TaskyAppGray,
                        focusedContainerColor = TaskyAppGray,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                    ),
                )

                OutlinedTextField(
                    value = loginState.password,
                    onValueChange = { onAction(LoginEvent.PasswordChanged(it)) },
                    placeholder = { Text(stringResource(id = R.string.register_screen_password_input)) },
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
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedContainerColor = TaskyAppGray,
                        focusedContainerColor = TaskyAppGray,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* TODO Handle sign up action */ },
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
                                // TODO Handle click on the clickable text
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
        loginState = LoginScreenState()
    )
}
