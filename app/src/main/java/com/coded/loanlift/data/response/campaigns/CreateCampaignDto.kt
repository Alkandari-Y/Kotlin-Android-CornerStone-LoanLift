package com.coded.loanlift.data.response.campaigns


import java.math.BigDecimal

data class CreateCampaignDto(
    val categoryId: Long,
    val title: String,
    val description: String,
    val goalAmount: BigDecimal,
    val campaignDeadline: String,
)
