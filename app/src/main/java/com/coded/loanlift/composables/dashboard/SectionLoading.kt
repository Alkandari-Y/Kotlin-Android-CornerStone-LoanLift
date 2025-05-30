package com.coded.loanlift.composables.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun SectionLoading(
    sectionTitle: String,
    onLinkClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    DashboardSection(
        sectionTitle = sectionTitle,
        onLinkClick = onLinkClick
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(2) { content() }
        }
    }
}