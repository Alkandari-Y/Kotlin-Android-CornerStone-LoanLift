package com.coded.loanlift.composables.campaignOwnerDetails

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.coded.loanlift.data.enums.CampaignDetailsTab

@Composable
fun CampaignTransactionsTabSelector(
    selectedTab: CampaignDetailsTab,
    onTabSelected: (CampaignDetailsTab) -> Unit
) {
    val visibleTabs = listOf(CampaignDetailsTab.INFO, CampaignDetailsTab.PLEDGES, CampaignDetailsTab.REPAYMENTS)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF2A2B2E))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        visibleTabs.forEach { tab ->
            val isSelected = tab == selectedTab
            Text(
                text = tab.name.lowercase().replaceFirstChar { it.uppercase() },
                color = if (isSelected) Color.White else Color.Gray,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable { onTabSelected(tab) }
            )
        }
    }
}
