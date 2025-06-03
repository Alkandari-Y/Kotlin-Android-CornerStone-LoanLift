package com.coded.loanlift.composables.campaignOwnerDetails

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.campaigns.CampaignCard
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails
import com.coded.loanlift.data.response.campaigns.toCampaignListItemResponse
import com.coded.loanlift.data.response.category.CategoryDto

@Composable
fun CampaignDetailsForOwner(
    campaign: CampaignOwnerDetails
) {
    CampaignCard(
        onCardClick = {},
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        campaign = campaign.toCampaignListItemResponse(),
        category = CategoryDto(campaign.categoryId, campaign.categoryName),
        contentScale = ContentScale.FillWidth,
        heightIn = 340.dp
    )
}