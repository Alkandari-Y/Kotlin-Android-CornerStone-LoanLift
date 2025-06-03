package com.coded.loanlift.screens.kyc

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.coded.loanlift.navigation.NavRoutes
import com.coded.loanlift.repositories.UserRepository
import com.coded.loanlift.viewModels.KycViewModel
import com.coded.loanlift.viewModels.UiStatus
import android.content.Context


@Composable
fun KycScreen(navController: NavHostController,

              viewModel: KycViewModel = viewModel()
) {
    val purple = Color(0xFF6200EE)
    val context = LocalContext.current

//    val viewModel: KycViewModel = viewModel(factory = KycViewModelFactory(context))


    val formState by viewModel.formState
    val isEditMode by viewModel.isEditMode
    val status by viewModel.status.collectAsState()

    LaunchedEffect(status) {
        when (status) {
            is UiStatus.Success -> {
                Toast.makeText(context, "KYC updated successfully", Toast.LENGTH_SHORT).show()
                navController.navigate(NavRoutes.NAV_ROUTE_DASHBOARD) {
                    popUpTo(NavRoutes.NAV_ROUTE_DASHBOARD) { inclusive = true }
                }
            }

            is UiStatus.Error -> {
                Toast.makeText(context, (status as UiStatus.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

            else -> {}
        }
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color=Color(0xFF0D0C1D))
    ) {
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
                modifier = Modifier
                    .align(Alignment.Start)
                    .clickable {
                      navController.popBackStack()
//                            navController.navigate(NavRoutes.NAV_ROUTE_DASHBOARD) {
//                                popUpTo(NavRoutes.NAV_ROUTE_DASHBOARD) { inclusive = true }



                    }
            )

            Text(
                "Profile",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = if (isEditMode) "Update Personal Information" else "Your Personal Information",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )


                TextField(
                    value = formState.firstName,
                    onValueChange = { viewModel.formState.value = formState.copy(firstName = it) },
                    label = { Text("First Name" ,color = Color.White) },

                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditMode,
                    isError = formState.firstNameError != null,
                    supportingText = {
                        formState.firstNameError?.let {
                            Text(
                                it,
                                color = Color.Red
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledTextColor =  Color.Gray,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.White
                    )
                )

                TextField(
                    value = formState.lastName,
                    onValueChange = { viewModel.formState.value = formState.copy(lastName = it) },
                    label = { Text("Last Name" ,color = Color.White) },

                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditMode,
                    isError = formState.lastNameError != null,
                    supportingText = {
                        formState.lastNameError?.let {
                            Text(
                                it,
                                color = Color.Red
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledTextColor =  Color.Gray,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.White
                    )
                )

                TextField(
                    value = formState.dateOfBirth,
                    onValueChange = {
                        viewModel.formState.value = formState.copy(dateOfBirth = it)
                    },
                    label = { Text("Date of Birth (dd-MM-yyyy)" ,color = Color.White) },

                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditMode,
                    isError = formState.dateOfBirthError != null,
                    supportingText = {
                        formState.dateOfBirthError?.let {
                            Text(
                                it,
                                color = Color.Red
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledTextColor =  Color.Gray,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.White
                    )
                )

                TextField(
                    value = formState.salary,

                    onValueChange = { viewModel.formState.value = formState.copy(salary = it) },
                    label = { Text("Salary" ,color = Color.White) },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditMode,
                    isError = formState.salaryError != null,
                    supportingText = { formState.salaryError?.let { Text(it, color = Color.Red) } },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        disabledTextColor =  Color.Gray,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.White
                    )
                )



                TextField(
                    value = formState.nationality,
                    label = { Text("Nationality" ,color = Color.White) },
                    onValueChange = {
                        viewModel.formState.value = formState.copy(nationality = it)
                    },

                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditMode,
                    isError = formState.nationalityError != null,
                    supportingText = {
                        formState.nationalityError?.let {
                            Text(
                                it,
                                color = Color.Red
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
disabledTextColor =  Color.Gray,
                        focusedIndicatorColor = Color.White,
                        unfocusedIndicatorColor = Color.Gray,
                        cursorColor = Color.White
                    )

                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        if (isEditMode) {
                            viewModel.submitKyc()
                        } else {
                            viewModel.isEditMode.value = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text(
                        text = if (isEditMode) "Save" else "Edit",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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