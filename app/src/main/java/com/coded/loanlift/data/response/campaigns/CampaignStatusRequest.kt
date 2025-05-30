package com.coded.loanlift.data.response.campaigns

import com.coded.loanlift.data.enums.CampaignStatus


data class CampaignStatusRequest(
    val name: CampaignStatus
)
