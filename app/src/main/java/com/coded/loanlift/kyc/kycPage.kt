package com.coded.loanlift.kyc

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coded.loanlift.kyc.kycViewModel.KycViewModel

import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.geometry.Offset
import com.coded.loanlift.kyc.kycViewModel.UiStatus
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context
import androidx.compose.ui.platform.LocalContext


@Composable
fun KycEditPage(viewModel: KycViewModel = viewModel()) {
    val purple = Color(0xFF6200EE)
    val status by viewModel.status.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(status) {
        when (status) {
            is UiStatus.Success -> {
                Toast.makeText(context, "KYC updated successfully", Toast.LENGTH_SHORT).show()
//                onNavigateHome()
            }
            is UiStatus.Error -> {
                Toast.makeText(context, (status as UiStatus.Error).message, Toast.LENGTH_SHORT).show()
            }
            else -> {}
        }
    }

    Box(  modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "< Back",
                color = purple,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.align(Alignment.Start)
            )

            Text("Edit KYC", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)

            TextField(
                value = viewModel.firstName.value,
                onValueChange = { viewModel.firstName.value = it },
                placeholder = { Text("First Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBottomBorder(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    cursorColor = Color.White,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary

                )
            )

            TextField(
                value = viewModel.lastName.value,
                onValueChange = { viewModel.lastName.value = it },
                placeholder = { Text("Last Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBottomBorder(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    cursorColor = Color.White,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary

                )
            )

            TextField(
                value = viewModel.dateOfBirth.value,
                onValueChange = { viewModel.dateOfBirth.value = it },
                placeholder = { Text("Date of Birth (dd-MM-yyyy)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBottomBorder(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    cursorColor = Color.White,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary


                )
            )

            TextField(
                value = viewModel.salary.value,
                onValueChange = { viewModel.salary.value = it },
                placeholder = { Text("Salary") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBottomBorder(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    cursorColor = Color.White,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary
                )
            )

            TextField(
                value = viewModel.nationality.value,
                onValueChange = { viewModel.nationality.value = it },
                placeholder = { Text("Nationality") },
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBottomBorder(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,

                    cursorColor = Color.White,
                    focusedTextColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = MaterialTheme.colorScheme.primary

                )
            )
        }

        Button(
            onClick = { viewModel.submitKyc() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = purple)
        ) {
            Text("Submit", color = Color.White)
        }
    }
}



fun Modifier.drawBottomBorder(strokeWidth: Dp = 1.dp, color: Color = Color.Gray): Modifier {
    return this.then(
        Modifier.drawBehind {
            val strokePx = strokeWidth.toPx()
            drawLine(
                color = color,
                start = Offset(0f, size.height - strokePx / 2),
                end = Offset(size.width, size.height - strokePx / 2),
                strokeWidth = strokePx
            )
        }
    )
}