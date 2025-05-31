package com.coded.loanlift.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

@Composable
fun LoadingDashboardScreen(navController: NavHostController) {
    LaunchedEffect(Unit) {
        delay(1500L)
        navController.navigate(NavRoutes.NAV_ROUTE_DASHBOARD) {
            popUpTo(NavRoutes.NAV_ROUTE_LOADING_DASHBOARD) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
