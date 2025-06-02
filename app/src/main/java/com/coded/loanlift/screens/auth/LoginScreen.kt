package com.coded.loanlift.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coded.loanlift.R
import com.coded.loanlift.formStates.auth.LoginFormState
import com.coded.loanlift.managers.TokenManager
import com.coded.loanlift.navigation.NavRoutes
import com.coded.loanlift.viewModels.AuthUiState
import com.coded.loanlift.viewModels.AuthViewModel
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavHostController,
    onForgotPasswordClick: () -> Unit
) {
    val uiState = viewModel.uiState.value
    val context = LocalContext.current
    val token = viewModel.token.value

    var showPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(TokenManager.isRememberMeEnabled(context)) }
    var formState by remember { mutableStateOf(LoginFormState()) }

    LaunchedEffect(token) {
        if (!token?.access.isNullOrBlank()) {
            if (TokenManager.decodeAccessToken(context)?.isActive == true) {
                navController.navigate(NavRoutes.NAV_ROUTE_LOADING_DASHBOARD) {
                    popUpTo(NavRoutes.NAV_ROUTE_LOGIN) { inclusive = true }
                }
            } else {
                navController.navigate(NavRoutes.NAV_ROUTE_EDIT_KYC) {
                    popUpTo(NavRoutes.NAV_ROUTE_LOGIN) { inclusive = true }
                }
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(color=Color(0xFF0D0C1D))
    ) {
        val screenHeight = maxHeight

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_no_bg),
                contentDescription = "Top Logo",
                modifier = Modifier
//                    .align(Alignment.TopEnd)
                    .height(screenHeight * 0.25f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = formState.username,
                onValueChange = { formState = formState.copy(username = it) },
                label = { Text("Username", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.usernameError != null,
                supportingText = {
                    formState.usernameError?.let {
                        Text(it, color = Color.Red)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = formState.password,
                onValueChange = {
                    formState = formState.copy(password = it).validate()
                },
                label = { Text("Password", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.passwordError != null,
                supportingText = {
                    formState.passwordError?.let {
                        Text(it, color = Color.Red)
                    }
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility",
                            tint = Color.White
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    cursorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color.White, checkmarkColor = Color.Black)
                    )
                    Text("Remember Me", color = Color.White, fontSize = 12.sp)
                }

                Text(
                    text = "Forgot Password ?",
                    color = Color.White,
                    modifier = Modifier.clickable { onForgotPasswordClick() },
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (uiState is AuthUiState.Error) {
                Text(
                    text = uiState.message,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Button(
                onClick = {
                    val validated = formState.validate()
                    if (validated.formIsValid) {
                        formState = validated
                        viewModel.login(validated.username, validated.password)
                        TokenManager.setRememberMe(context, rememberMe)
                    } else {
                        formState = validated
                        Toast.makeText(context, "Fix errors before submitting", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = RectangleShape
            ) {
                Text("Log In", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Text("Don't have an account ? ", color = Color.Gray)
                Text(
                    text = "Sign Up",
                    modifier = Modifier.clickable {
                        navController.navigate(NavRoutes.NAV_ROUTE_SIGNUP)
                    },
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
