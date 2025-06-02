package com.coded.loanlift.data.mappers

import com.coded.loanlift.data.response.campaigns.CampaignDetailResponse
import com.coded.loanlift.data.response.campaigns.CampaignListItemResponse
import com.coded.loanlift.data.response.campaigns.CampaignPublicDetails

fun CampaignPublicDetails.toCampaignListItemResponse(): CampaignListItemResponse {
    return CampaignListItemResponse(
        id = this.id,
        createdBy = createdBy,
        categoryId = categoryId,
        title = title,
        goalAmount = goalAmount,
        status = status,
        campaignDeadline = campaignDeadline,
        imageUrl = imageUrl,
        amountRaised = amountRaised,
        categoryName = categoryName
    )
}