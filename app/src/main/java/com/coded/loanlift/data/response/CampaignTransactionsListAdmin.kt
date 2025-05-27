package com.coded.loanlift.data.response

import java.math.BigDecimal
import java.time.LocalDate

data class CampaignTransactionDetailsDto(
    val sourceAccountId: Long,
    val sourceAccountNumber: String,
    val destinationAccountId: Long,
    val destinationAccountNumber: String,
    val amount: BigDecimal,
    val transactionDate: LocalDate,
    val transactionType: PledgeTransactionType,
    val pledgeId: Long
)

data class CampaignWithTransactionsListAdminDto(
    val id: Long,
    val createdBy: Long,
    val accountId: Long,
    val title: String,
    val goalAmount: BigDecimal,
    val interestRate: BigDecimal,
    val repaymentMonths: Int,
    val status: CampaignStatus,
    val transactions: List<CampaignTransactionDetailsDto>
)


enum class PledgeTransactionType {
    FUNDING,     // Money going into pledge
    REFUND,      // Money refunded to user
    REPAYMENT    // Repayment from campaign
}
