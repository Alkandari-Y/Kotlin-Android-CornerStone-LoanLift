package com.coded.loanlift.data.response.campaigns

import com.coded.loanlift.data.enums.CampaignStatus
import java.math.BigDecimal

data class CampaignOwnerDetails(
    val id: Long,
    val createdBy: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val description: String,
    val goalAmount: BigDecimal,
    val interestRate: BigDecimal,
    val repaymentMonths: Int,
    val monthlyInstallment: BigDecimal,
    var amountRaised: BigDecimal,
    val bankFee: BigDecimal,
    val netToLenders: BigDecimal,
    val status: CampaignStatus,
    val submittedAt: String,
    val campaignDeadline: String,
    val imageUrl: String?,
    val accountId: Long
) : CampaignDetailResponse
