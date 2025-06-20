package com.coded.loanlift.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coded.loanlift.R
import com.coded.loanlift.formStates.auth.RegisterFormState
import com.coded.loanlift.navigation.NavRoutes
import com.coded.loanlift.viewModels.AuthUiState
import com.coded.loanlift.viewModels.AuthViewModel

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel = viewModel(),
    navController: NavHostController
) {
    val uiState = viewModel.uiState.value
    val fieldErrors = viewModel.registerFieldErrors.value
    val context = LocalContext.current

    var showPassword by remember { mutableStateOf(false) }
    var showConfirmedPassword by remember { mutableStateOf(false) }
    var formState by remember { mutableStateOf(RegisterFormState()) }

    LaunchedEffect(fieldErrors) {
        if (fieldErrors.isNotEmpty()) {
            formState = formState.applyServerErrors(fieldErrors)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is AuthUiState.Success) {
            Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
            
            navController.navigate(NavRoutes.NAV_ROUTE_EDIT_KYC) {
                popUpTo(NavRoutes.NAV_ROUTE_SIGNUP) { inclusive = true }
            }
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(color = Color(0xFF0D0C1D))) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

        Image(
            painter = painterResource(id = R.drawable.logo_no_bg),
            contentDescription = "Top Background Logo",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .height(screenHeight * 0.2f),
            contentScale = ContentScale.Fit
        )

        Image(
            painter = painterResource(id = R.drawable.logo_no_bg),
            contentDescription = "Top Background Logo",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .scale(3f).absoluteOffset(60.dp,120.dp),
            contentScale = ContentScale.Fit
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Hi !", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2))
            Text(
                text = "Welcome",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Let's create an account", fontSize = 16.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            TextField(
                value = formState.username,
                onValueChange = { formState = formState.copy(username = it) },
                label = { Text("Username" ,color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.usernameError != null,
                supportingText = {
                    formState.usernameError?.let {
                        Text(text = it, color = Color.Red)
                    }
                },  colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    errorContainerColor = Color.Transparent,
                    errorTextColor =Color.White ,
                    cursorColor = Color.White
                )
            )

            TextField(
                value = formState.email,
                onValueChange = { formState = formState.copy(email = it).validate() },
                label = { Text("Email",color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.usernameError != null,
                supportingText = {
                    formState.usernameError?.let {
                        Text(text = it, color = Color.Red)
                    }
                },  colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    errorContainerColor = Color.Transparent,
                    errorTextColor =Color.White ,
                    cursorColor = Color.White
                )
            )

            TextField(
                value = formState.civilId,
                onValueChange = { formState = formState.copy(civilId = it).validate() },
                label = { Text("Civil ID",color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.civilIdError != null,
                supportingText = { formState.civilIdError?.let { Text(it, color = Color.Red) } },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    errorContainerColor = Color.Transparent,
                    errorTextColor =Color.White ,
                    cursorColor = Color.White
                )
            )


            TextField(
                value = formState.password,
                onValueChange = { formState = formState.copy(password = it).validate() },
                label = { Text("Password",color = Color.White) },
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
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.Gray,
                    errorContainerColor = Color.Transparent,
                    errorTextColor =Color.White ,
                    cursorColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            TextField(
                value = formState.confirmPassword,
                onValueChange = { formState = formState.copy(confirmPassword = it).validate() },
                label = { Text("Confirm Password",color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                isError = formState.confirmPasswordError != null,
                supportingText = {
                    formState.confirmPasswordError?.let {
                        Text(it, color = Color.Red)
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.White,
                    errorContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Gray,
                    errorTextColor =Color.White ,
                    cursorColor = Color.White
                ),
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

            if (uiState is AuthUiState.Error) {
                Text(
                    text = uiState.message,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }


            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    formState = formState.validate()
                    if (formState.formIsValid) {
                        viewModel.register(
                            username = formState.username,
                            email = formState.email,
                            password = formState.password,
                            civilId = formState.civilId
                        )
                    } else {
                        Toast.makeText(context, "Fix errors before submitting", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                shape = RectangleShape
            ) {
                Text(text = "Sign Up", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = "Have an account ? ", color = Color.Gray)
                Text(
                    text = "Log In",
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.popBackStack(NavRoutes.NAV_ROUTE_LOGIN, false)
                    },
                )
            }
        }
    }
}
