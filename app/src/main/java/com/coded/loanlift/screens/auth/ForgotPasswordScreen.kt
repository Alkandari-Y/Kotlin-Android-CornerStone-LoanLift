package com.coded.loanlift.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coded.loanlift.navigation.NavRoutes

@Composable
fun ForgotPasswordScreen(
    onSubmitClick: () -> Unit,
) {
    var identifier by remember { mutableStateOf("") }
    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(color = Color(0xFF0D0C1D))) {
        val screenHeight = maxHeight
        val screenWidth = maxWidth

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Oh, no !",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
        Text(
            text = "I forgot",
            fontSize = 60.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1976D2)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Enter your email, phone, or username and we'll\nsend you a link to change a new password",
            fontSize = 14.sp,
            color = Color.LightGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        TextField(
            value = identifier,
            onValueChange = { identifier = it },
            label = { Text("Username, Email or Phone Number",color = Color.White) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSubmitClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
            shape = RectangleShape
        ) {
            Text("Forgot Password", fontWeight = FontWeight.Bold)
        }

    }
} }
