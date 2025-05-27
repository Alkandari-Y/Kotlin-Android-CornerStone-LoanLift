package com.coded.loanlift.data.response

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
