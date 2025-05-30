package com.coded.loanlift.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.dashboardscreen.AccountsSection
import com.coded.loanlift.composables.dashboardscreen.CampaignsSection
import com.coded.loanlift.composables.dashboardscreen.PledgesSection
import com.coded.loanlift.composables.ui.TopBar
import com.coded.loanlift.managers.TokenManager
import com.coded.loanlift.viewModels.DashboardViewModel


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    navController: NavHostController,
    onLogoutClick: () -> Unit
) {
    val accounts by viewModel.accounts.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
        viewModel.fetchAccounts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1B1E))
            .padding(16.dp)
    ) {
        TopBar(
            onProfileClick = {
                navController.navigate("profile")
            },
            onLogoutClick = {
                TokenManager.clearToken(context)
                onLogoutClick()
            }
        )
        Spacer(modifier = Modifier.height(24.dp))
        AccountsSection(accounts = accounts)
        Spacer(modifier = Modifier.height(24.dp))
        CampaignsSection()
        Spacer(modifier = Modifier.height(24.dp))
        PledgesSection()
    }
}