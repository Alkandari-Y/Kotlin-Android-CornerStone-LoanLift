package com.coded.loanlift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.coded.loanlift.login.LoginScreen
import com.coded.loanlift.signUp.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    SignUpScreen(
                        onSignUpClick = { /* TODO */ },
                        onLoginClick = { /* TODO */ }
                    )

//                    LoginScreen(
//                        onLoginClick = {}
//    onLoginClick: () -> Unit,
//    onSignUpClick: () -> Unit,
//    onForgotPasswordClick: () -> Unit
//)
                }
            }
        }
    }
}