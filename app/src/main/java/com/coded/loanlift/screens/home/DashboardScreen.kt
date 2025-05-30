package com.coded.loanlift.screens.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.dashboardscreen.AccountsSection
import com.coded.loanlift.composables.dashboardscreen.CampaignsSection
import com.coded.loanlift.composables.dashboardscreen.PledgesSection
import com.coded.loanlift.composables.dashboardscreen.TopBar
import com.coded.loanlift.viewModels.DashboardViewModel


@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    navController: NavHostController,
) {
    var userName by remember { mutableStateOf("Yousef Alkandari") }
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
        TopBar(userName)
        Spacer(modifier = Modifier.height(24.dp))
        AccountsSection(accounts = accounts)
        Spacer(modifier = Modifier.height(24.dp))
        CampaignsSection()
        Spacer(modifier = Modifier.height(24.dp))
        PledgesSection()

        Button(onClick = {
            viewModel.fetchAccounts()
        }) {
            Text("Test Authenticated Call")
        }
    }
}