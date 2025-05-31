package com.coded.loanlift.data.response.campaigns

import com.coded.loanlift.data.enums.CampaignStatus
import java.math.BigDecimal
import java.time.LocalDate

data class CampaignDetailsBasicAdmin(
    val id: Long,
    val createdBy: Long,
    val accountId: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val description: String,
    val goalAmount: BigDecimal,
    val interestRate: BigDecimal,
    val repaymentMonths: Int,
    val status: CampaignStatus,
    val submittedAt: String,
    val campaignDeadline: String,
    val imageUrl: String,
    val files: List<FileDto> = emptyList(),
)
