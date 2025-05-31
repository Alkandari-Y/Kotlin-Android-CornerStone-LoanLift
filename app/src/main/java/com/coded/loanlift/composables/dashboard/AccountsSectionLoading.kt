package com.coded.loanlift.composables.dashboardscreen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.accounts.SkeletonAccountCard
import com.coded.loanlift.composables.ui.SectionLoading


@Composable
fun AccountsSectionLoading() {
    val sectionTitle = "My Accounts"

    SectionLoading(
        sectionTitle = sectionTitle,
        onLinkClick = { }
    ) {
        SkeletonAccountCard(
            modifier = Modifier
                .padding(end = 8.dp)
                .width(200.dp)
        )
    }
}