package com.coded.loanlift.screens.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coded.loanlift.formStates.LoginFormState
import com.coded.loanlift.formStates.RegisterFormState
import com.coded.loanlift.navigation.NavRoutesEnum
import com.coded.loanlift.viewModels.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavHostController
) {
    val uiState = viewModel.uiState.value
    val context = LocalContext.current
    val token = viewModel.token.value

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmedPassword by remember { mutableStateOf(false) }
    var formState by remember { mutableStateOf(RegisterFormState()) }

    LaunchedEffect(token) {
        if (token?.access?.isNotBlank() == true) {
            navController.navigate(NavRoutesEnum.NAV_ROUTE_LOADING_DASHBOARD.value) {
                popUpTo(NavRoutesEnum.NAV_ROUTE_LOGIN.value) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hi !", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(
            text = "Welcome",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Let's create an account", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = formState.username,
            onValueChange = { formState = formState.copy(username = it) },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.usernameError != null,
            supportingText = {
                formState.usernameError?.let {
                    Text(text = it, color = Color.Red)
                }
            }
        )

        OutlinedTextField(
            value = formState.email,
            onValueChange = { formState = formState.copy(email = it).validate() },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.usernameError != null,
            supportingText = {
                formState.usernameError?.let {
                    Text(text = it, color = Color.Red)
                }
            }
        )

        OutlinedTextField(
            value = formState.civilId,
            onValueChange = { formState = formState.copy(civilId = it).validate() },
            label = { Text("Civil ID") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.civilIdError != null,
            supportingText = { formState.civilIdError?.let { Text(it, color = Color.Red) } }
        )


        OutlinedTextField(
            value = formState.password,
            onValueChange = { formState = formState.copy(password = it).validate() },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.passwordError != null,
            supportingText = { formState.passwordError?.let { Text(it, color = Color.Red) } },
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Toggle password visibility"
                    )
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Text(
            text = "Must contain a number and least of 6 characters",
            fontSize = 12.sp,
            color = Color(0xFF8E24AA)
        )

        OutlinedTextField(
            value = formState.confirmPassword,
            onValueChange = { formState = formState.copy(confirmPassword = it).validate() },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showConfirmedPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showConfirmedPassword = !showConfirmedPassword }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "Toggle confirm password visibility"
                    )
                }
            }
        )

        Text(
            text = "Must contain a number and least of 6 characters",
            fontSize = 12.sp,
            color = Color(0xFF8E24AA)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                formState = formState.validate()
                if (formState.formIsValid) {
                    Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
                        navController.navigate(NavRoutesEnum.NAV_ROUTE_LOADING_DASHBOARD.value) {
                            popUpTo(NavRoutesEnum.NAV_ROUTE_SIGNUP.value) { inclusive = true }
                        }
                } else {
                    Toast.makeText(context, "Fix errors before submitting", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text(text = "Sign Up", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(text = "Have an account ? ", color = Color.Gray)
            Text(
                text = "Log In",
                color = Color(0xFF8E24AA),
                modifier = Modifier.clickable {
                    navController.popBackStack(NavRoutesEnum.NAV_ROUTE_LOGIN.value, false)
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}
