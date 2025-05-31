package com.coded.loanlift.composables.dashboard

import androidx.compose.runtime.Composable
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard

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