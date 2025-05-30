package com.coded.loanlift.composables.dashboardscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard

@Composable
fun CampaignsSectionLoading() {
    DashboardSection(
        sectionTitle = "My Campaigns",
        onLinkClick = {  }
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(2) {
                SkeletonCampaignCard()
            }
        }
    }
}