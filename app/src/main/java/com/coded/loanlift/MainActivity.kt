package com.coded.loanlift

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.coded.loanlift.dashboardscreen.AccountDetailsScreen
import com.coded.loanlift.dashboardscreen.DashboardScreen
import com.coded.loanlift.navigation.AppHost
import com.coded.loanlift.ui.theme.LoanLiftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppHost()
                }
            }
        }
    }
}

sealed class ScreenState {
    object AccountDetails : ScreenState()
    data class CampaignDetails(val campaignTitle: String) : ScreenState()
}


@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    LoanLiftTheme {
        DashboardScreen()
    }
}