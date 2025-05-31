package com.coded.loanlift.composables.dashboard

import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.accounts.SkeletonAccountCard


@Composable
fun AccountsSectionLoading() {
    val sectionTitle = "My Accounts"

    SectionLoading(
        sectionTitle = sectionTitle,
        onLinkClick = { }
    ) {
        SkeletonAccountCard(
            modifier = Modifier
                .width(280.dp)
        )
    }
}