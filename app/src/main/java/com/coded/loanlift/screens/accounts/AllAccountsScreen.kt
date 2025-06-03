package com.coded.loanlift.screens.accounts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.coded.loanlift.viewModels.DashboardViewModel

@Composable
fun AllAccountsScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit,
    onAccountClick: (String) -> Unit
) {
    Text(text = "hello")
}