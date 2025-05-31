package com.coded.loanlift.composables.dashboardscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.coded.loanlift.data.enums.AccountType
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.screens.accounts.AccountDetailsScreen
import com.coded.loanlift.screens.campaigns.CampaignDetailsScreen
import java.math.BigDecimal

@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf<ScreenState>(ScreenState.AccountDetails) }


    val account = AccountDto(
        id = 1L,
        accountNumber = "1111",
        name = "Meshal Alquraini",
        balance = BigDecimal("3000"),
        active = true,
        ownerId = 1,
        ownerType = AccountType.CAMPAIGN
    )

    when (val screen = currentScreen) {
        is ScreenState.AccountDetails -> AccountDetailsScreen(
            onCampaignClick = { campaignTitle ->
                currentScreen = if (account.ownerType == AccountType.CAMPAIGN) {
                    ScreenState.CampaignDetails(campaignTitle)
                } else {
                    return@AccountDetailsScreen
                }
            }
        )

        is ScreenState.CampaignDetails -> CampaignDetailsScreen(
            campaignTitle = screen.campaignTitle,
            onBack = { currentScreen = ScreenState.AccountDetails }
        )
    }
}


sealed class ScreenState {
    object AccountDetails : ScreenState()
    data class CampaignDetails(val campaignTitle: String) : ScreenState()
}