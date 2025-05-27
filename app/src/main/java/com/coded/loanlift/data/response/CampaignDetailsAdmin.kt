package com.coded.loanlift.data.response



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


enum class CampaignStatus {
    NEW,                // Status of a new Campaign, users can make edits
    PENDING,            // Status of a campaign under admin review
    REJECTED,           // Isn't approved for launch
    ACTIVE,             // Available to users for funding
    FUNDED,             // Reached or surpassed its goal and deadline
    FAILED,             // Didnt reach its goalAmount by deadline
    COMPLETED,          // Finished repayment of funded goal amount plus interest
    DEFAULTED           // Didn't pay back all its funded goal amount plus interest within repayment months
}