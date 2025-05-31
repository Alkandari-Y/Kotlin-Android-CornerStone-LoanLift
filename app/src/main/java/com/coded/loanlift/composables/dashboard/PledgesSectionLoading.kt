package com.coded.loanlift.composables.dashboard

import androidx.compose.runtime.Composable
import com.coded.loanlift.composables.pledges.SkeletonPledgeCard

@Composable
fun PledgesSectionLoading() {
    val sectionTitle = "My Pledges"

    SectionLoading(
        sectionTitle = sectionTitle,
        onLinkClick = { }
    ) {
        SkeletonPledgeCard()
    }
}