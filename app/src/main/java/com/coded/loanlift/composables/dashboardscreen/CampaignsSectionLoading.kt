package com.coded.loanlift.composables.dashboardscreen

import androidx.compose.runtime.Composable
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard
import com.coded.loanlift.composables.ui.SectionLoading

@Composable
fun CampaignsSectionLoading() {
    val sectionTitle = "My Campaigns"

    SectionLoading(
        sectionTitle = sectionTitle,
        onLinkClick = { }
    ) {
        SkeletonCampaignCard()
    }
}