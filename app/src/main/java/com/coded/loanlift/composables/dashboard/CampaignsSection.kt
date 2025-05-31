package com.coded.loanlift.composables.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.campaigns.CampaignCard
import com.coded.loanlift.composables.campaigns.CreateCampaignCard
import com.coded.loanlift.data.response.campaigns.CampaignListItemResponse
import com.coded.loanlift.repositories.CategoryRepository

@Composable
fun CampaignsSection(
    campaigns: List<CampaignListItemResponse>,
    navController: NavHostController,
    onCampaignClick: (Long) -> Unit,
    onCampaignCreateClick: () -> Unit,
) {
    val categories = CategoryRepository.categories
    DashboardSection(
        sectionTitle = "My Campaigns",
        onLinkClick = { /* TODO */ }
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(campaigns) { campaign ->
                CampaignCard(
                    modifier = Modifier.width(280.dp),
                    campaign = campaign,
                    category = categories.find { it.id == campaign.categoryId },
                    onCardClick = {
                        onCampaignClick(campaign.id)
                    }
                )
            }

            item {
                CreateCampaignCard(
                    modifier = Modifier.width(280.dp),
                    onCreateClick = onCampaignCreateClick
                )
            }
        }
    }
}
