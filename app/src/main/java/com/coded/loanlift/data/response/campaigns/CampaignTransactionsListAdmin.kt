package com.coded.loanlift.data.response.campaigns

import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.enums.PledgeTransactionType
import java.math.BigDecimal
import java.time.LocalDate

data class CampaignTransactionDetailsDto(
    val sourceAccountId: Long,
    val sourceAccountNumber: String,
    val destinationAccountId: Long,
    val destinationAccountNumber: String,
    val amount: BigDecimal,
    val transactionDate: String,
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


