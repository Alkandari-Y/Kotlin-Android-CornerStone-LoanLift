package com.coded.loanlift.kyc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coded.loanlift.kyc.kycViewModel.KycViewModel



@Composable
fun KycEditPage(viewModel: KycViewModel= viewModel()) {
//    val status by viewModel.status.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Edit KYC", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = viewModel.firstName.value,
            onValueChange = { viewModel.firstName.value = it },
            label = { Text("First Name") }
        )

        OutlinedTextField(
            value = viewModel.lastName.value,
            onValueChange = { viewModel.lastName.value = it },
            label = { Text("Last Name") }
        )

        OutlinedTextField(
            value = viewModel.dateOfBirth.value,
            onValueChange = { viewModel.dateOfBirth.value = it },
            label = { Text("Date of Birth (yyyy-MM-dd)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)

        )

        OutlinedTextField(
            value = viewModel.salary.value,
            onValueChange = { viewModel.salary.value = it },
            label = { Text("Salary") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)

        )

        OutlinedTextField(
            value = viewModel.nationality.value,
            onValueChange = { viewModel.nationality.value = it },
            label = { Text("Nationality") },

        )


        Button (
            onClick = { viewModel.submitKyc()},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
        ) {
            Text("Submit")
        }

//        when (status) {
//            is KycViewModel.UiStatus.Loading -> CircularProgressIndicator()
//            is KycViewModel.UiStatus.Success -> Text("KYC updated successfully!", color = Color.Green)
//            is KycViewModel.UiStatus.Error -> Text(
//                "Error: ${(status as KycViewModel.UiStatus.Error).message}",
//                color = Color.Red
//            )
//            else -> {}
//        }
    }
}