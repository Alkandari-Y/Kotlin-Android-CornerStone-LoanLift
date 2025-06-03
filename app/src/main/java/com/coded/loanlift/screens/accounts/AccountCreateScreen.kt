package com.coded.loanlift.screens.accounts

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coded.loanlift.navigation.NavRoutes
import com.coded.loanlift.viewModels.AccountViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountCreateScreen(
    navController: NavController,
    viewModel: AccountViewModel = viewModel()
) {
    val context = LocalContext.current
    val formState by viewModel.formState.collectAsState()
    val uiState by viewModel.accountUiState.collectAsState()
    val shouldNavigate by viewModel.shouldNavigate.collectAsState()
    val isLoading = uiState is AccountViewModel.AccountCreateUiState.Loading

    if (shouldNavigate) {
        LaunchedEffect(true) {
            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
            viewModel.resetNavigationFlag()
            navController.navigate(NavRoutes.NAV_ROUTE_DASHBOARD) {
                popUpTo(NavRoutes.NAV_ROUTE_DASHBOARD) { inclusive = true }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1A1B1E))) {
        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(
                title = { Text("Create New Account", color = Color.White) },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(
//                            Icons.AutoMirrored.Filled.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A2B2E))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1A1B1E)) // Dark background
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top
            ) {
//                Text(
//                    "Create New Account",
//                    style = MaterialTheme.typography.headlineMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White
//                )

                Spacer(modifier = Modifier.height(24.dp))

                TextField(
                    value = formState.name,
                    onValueChange = viewModel::updateName,
                    label = { Text("Account Name", color = Color.White) },
                    isError = formState.nameError != null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        errorContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Gray,
                        errorTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                formState.nameError?.let {
                    Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = formState.initialBalance,
                    onValueChange = viewModel::updateInitialBalance,
                    label = { Text("Initial Balance", color = Color.White) },
                    isError = formState.balanceError != null,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.White,
                        errorContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Gray,
                        errorTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                formState.balanceError?.let {
                    Text(it, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate(NavRoutes.NAV_ROUTE_DASHBOARD) {
                                popUpTo(NavRoutes.NAV_ROUTE_DASHBOARD) { inclusive = true }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = viewModel::submitAccount,
                        modifier = Modifier.weight(1f),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6200EE),
                            contentColor = Color.White
                        )
                    ) {
                        Text(if (isLoading) "Creating..." else "Create")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState is AccountViewModel.AccountCreateUiState.Error) {
                    Text(
                        (uiState as AccountViewModel.AccountCreateUiState.Error).message,
                        color = Color.Red
                    )
                }

                if (isLoading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }
        }
    }}