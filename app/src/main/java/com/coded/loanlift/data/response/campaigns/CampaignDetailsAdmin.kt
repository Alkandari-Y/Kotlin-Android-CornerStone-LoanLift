package com.coded.loanlift.data.response.campaigns



import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.response.pledges.PledgeOverviewDto
import java.math.BigDecimal
import java.time.LocalDate

data class CampaignDetailsAdmin (
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
    val submittedAt: LocalDate,
    val campaignDeadline: LocalDate,
    val imageUrl: String,
    var amountRaised: BigDecimal,
    val files: List<FileDto>,
    val pledges: List<PledgeOverviewDto>
)


