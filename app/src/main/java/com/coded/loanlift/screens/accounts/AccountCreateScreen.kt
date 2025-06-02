package com.coded.loanlift.screens.accounts

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coded.loanlift.navigation.NavRoutes
import com.coded.loanlift.viewModels.AccountViewModel

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Create New Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = formState.name,
            onValueChange = viewModel::updateName,
            label = { Text("Account Name") },
            isError = formState.nameError != null,
            modifier = Modifier.fillMaxWidth()
        )
        formState.nameError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = formState.initialBalance,
            onValueChange = viewModel::updateInitialBalance,
            label = { Text("Initial Balance") },
            isError = formState.balanceError != null,
            modifier = Modifier.fillMaxWidth()
        )
        formState.balanceError?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
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
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Cancel")
            }

            Button(
                onClick = viewModel::submitAccount,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text(if (isLoading) "Creating..." else "Create")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState is AccountViewModel.AccountCreateUiState.Error) {
            Text(
                (uiState as AccountViewModel.AccountCreateUiState.Error).message,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator()
        }
    }
}
