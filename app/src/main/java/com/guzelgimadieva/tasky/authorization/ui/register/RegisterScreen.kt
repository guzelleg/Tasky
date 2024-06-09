package com.guzelgimadieva.tasky.authorization.ui.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.guzelgimadieva.tasky.R
import com.guzelgimadieva.tasky.authorization.ui.components.InputTextField

@Composable
fun RegisterScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: RegisterScreenViewModel = hiltViewModel()
    val registerState by viewModel.registerState.collectAsStateWithLifecycle()
    RegisterScreenContent(
        onAction = viewModel::onEvent,
        registerState = registerState,
        onBackClick = onBackClick,
    )
}
@Composable
fun RegisterScreenContent (
    onAction: (RegisterEvent) -> Unit,
    registerState: RegisterScreenState,
    onBackClick: () -> Unit,
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
            style = MaterialTheme.typography.headlineLarge,
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
                    value = registerState.username,
                    onValueChange = { onAction(RegisterEvent.EmailChanged(it)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.register_screen_name_input),
                            style = TextStyle(color =  LocalTextStyle.current.color.copy(alpha = 0.5f))
                        )
                    },
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                )

                InputTextField(
                    value = registerState.email,
                    onValueChange = { onAction(RegisterEvent.EmailChanged(it)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.register_screen_email_input),
                            style = TextStyle(color = Color.Black.copy(alpha = 0.5f))
                        )
                    },
                    visualTransformation = VisualTransformation.None,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                )

                InputTextField(
                    value = registerState.password,
                    onValueChange = { onAction(RegisterEvent.PasswordChanged(it)) },
                    placeholder = {
                        Text(
                            text = stringResource(id = R.string.register_screen_password_input),
                            style = TextStyle(color = Color.Black.copy(alpha = 0.5f))
                        )
                    },
                    visualTransformation = if (registerState.passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image =
                            if (registerState.passwordVisible) Icons.Filled.Visibility else Icons.Default.VisibilityOff
                        IconButton(onClick = { onAction(RegisterEvent.PasswordVisibilityChanged) }) {
                            Icon(imageVector = image, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { /* TODO Handle register action */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text(
                        text = stringResource(id = R.string.welcome_screen_getStarted_button).uppercase(),
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Box(modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 32.dp),)
                {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(65.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.Black),
                    ) {
                        Icon(Icons.Filled.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = Color.White)
                    }
                }
            }
        }
    }
}