package com.coded.loanlift.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.dashboardscreen.AccountsSection
import com.coded.loanlift.composables.dashboardscreen.AccountsSectionLoading
import com.coded.loanlift.composables.dashboardscreen.CampaignsSection
import com.coded.loanlift.composables.dashboardscreen.PledgesSection
import com.coded.loanlift.composables.ui.TopBar
import com.coded.loanlift.managers.TokenManager
import com.coded.loanlift.viewModels.AccountsUiState
import com.coded.loanlift.viewModels.CampaignsUiState
import com.coded.loanlift.viewModels.DashboardViewModel


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    navController: NavHostController,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    val accountsUiState by viewModel.accountsUiState.collectAsState()
    val campaignsUiState by viewModel.campaignsUiState.collectAsState()
    val pledgesUiState by viewModel.pledgesUiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
        viewModel.fetchAccounts()
        viewModel.fetchCampaigns()
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

        when (val state = accountsUiState) {
            is AccountsUiState.Loading -> AccountsSectionLoading()
            is AccountsUiState.Success -> AccountsSection(accounts = state.accounts)
            is AccountsUiState.Error -> Text(
                text = "Failed to load accounts: ${state.message}",
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        when (val state = campaignsUiState) {
            is CampaignsUiState.Loading -> CircularProgressIndicator()
            is CampaignsUiState.Success -> CampaignsSection(campaigns = state.campaigns)
            is CampaignsUiState.Error -> Text(
                text = "Failed to load campaigns: ${state.message}",
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        PledgesSection()
    }
}