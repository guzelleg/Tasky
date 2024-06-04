package com.guzelgimadieva.tasky.authorization.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.guzelgimadieva.tasky.core.theme.TaskyAppGray

@Composable
fun InputTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit,
    visualTransformation: VisualTransformation,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(10.dp),
    colors: TextFieldColors = TextFieldDefaults.colors(
        unfocusedContainerColor = TaskyAppGray,
        focusedContainerColor = TaskyAppGray,
        unfocusedIndicatorColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        modifier = modifier,
        shape = shape,
        colors = colors,
    )
}