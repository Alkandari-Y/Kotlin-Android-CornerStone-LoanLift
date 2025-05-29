package com.coded.loanlift.data.response.campaigns

import com.coded.loanlift.data.enums.CampaignStatus
import java.math.BigDecimal

data class CampaignUpdateRequestAdmin(
    val title: String,
    val description: String,
    val categoryId: Long,
    val goalAmount: BigDecimal,
    val interestRate: BigDecimal,
    val repaymentMonths: Int,
    val campaignDeadline: String,
    val status: CampaignStatus
)
