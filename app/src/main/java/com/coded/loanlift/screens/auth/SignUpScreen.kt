package com.coded.loanlift.screens.auth

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coded.loanlift.navigation.NavRoutesEnum
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    navController: NavHostController
) {
    var emailOrPhone by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val passwordVisible = remember { mutableStateOf(false) }
    val confirmPasswordVisible = remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hi !", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Text(text = "Welcome", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Let's create an account", fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))



        OutlinedTextField(
            value = emailOrPhone,
            onValueChange = { emailOrPhone = it },
            label = { Text("Email or Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                    Icon(imageVector = Icons.Filled.Visibility, contentDescription = "Toggle password visibility")
                }
            }
        )
        Text(
            text = "Must contain a number and least of 6 characters",
            fontSize = 12.sp,
            color = Color(0xFF8E24AA)
        )

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { confirmPasswordVisible.value = !confirmPasswordVisible.value }) {
                    Icon(imageVector = Icons.Filled.Visibility, contentDescription = "Toggle confirm password visibility")
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
                Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                    delay(1000L)
                    navController.navigate(NavRoutesEnum.NAV_ROUTE_LOADING_DASHBOARD.value) {
                        popUpTo(NavRoutesEnum.NAV_ROUTE_SIGNUP.value) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text("Sign Up")
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
