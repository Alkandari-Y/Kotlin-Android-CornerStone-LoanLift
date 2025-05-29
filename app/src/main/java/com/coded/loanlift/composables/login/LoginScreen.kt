package com.coded.loanlift.composables.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coded.loanlift.formStates.LoginFormState
import com.coded.loanlift.navigation.NavRoutesEnum
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    onForgotPasswordClick: () -> Unit
) {

    var showPassword by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    var formState by remember { mutableStateOf(LoginFormState()) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LoanLift",
            style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00BCD4))
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = formState.username,
            onValueChange = { formState = formState.copy(username = it) },
            label = { Text("Username, Email or Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.usernameError.isNullOrBlank().not()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = formState.password,
            onValueChange = { formState = formState.copy(password = it) },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = "Toggle password visibility")
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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
                    onCheckedChange = { rememberMe = it }
                )
                Text(text = "Remember Me", color = Color.White)
            }

            Text(
                text = "Forgot Password ?",
                modifier = Modifier.clickable { onForgotPasswordClick() },
                color = Color(0xFFB39DDB),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                    delay(1000L)
                    navController.navigate(NavRoutesEnum.NAV_ROUTE_LOADING_DASHBOARD.value) {
                        popUpTo(NavRoutesEnum.NAV_ROUTE_LOGIN.value) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B1FA2))
        ) {
            Text(text = "Log In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Text(text = "Don't have an account ? ", color = Color.Gray)
            Text(
                text = "Sign Up",
                modifier = Modifier.clickable {
                    navController.navigate(NavRoutesEnum.NAV_ROUTE_SIGNUP.value)
                },
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold
            )
        }
    }
}


