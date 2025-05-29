package com.coded.loanlift.composables.dashboardscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun DashboardScreen() {
    var userName by remember { mutableStateOf("Yousef Alkandari") }
    var mainBalance by remember { mutableStateOf("30.560 KWD") }
    var secondaryBalance by remember { mutableStateOf("50.300 KWD") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1B1E))
            .padding(16.dp)
    ) {
        TopBar(userName)
        Spacer(modifier = Modifier.height(24.dp))
        AccountsSection(mainBalance, secondaryBalance)
        Spacer(modifier = Modifier.height(24.dp))
        CampaignsSection()
        Spacer(modifier = Modifier.height(24.dp))
        PledgesSection()
    }
}