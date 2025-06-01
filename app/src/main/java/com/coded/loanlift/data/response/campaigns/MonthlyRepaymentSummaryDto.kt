package com.coded.loanlift.data.response.campaigns

import com.coded.loanlift.data.enums.PledgeTransactionType
import com.coded.loanlift.data.enums.TransactionType
import java.math.BigDecimal
import java.time.Instant
import java.time.YearMonth

data class MonthlyRepaymentSummaryDto(
    val month: String,
    val totalPaidWithBankFee: BigDecimal,
    val pledgeTransactionType: PledgeTransactionType = PledgeTransactionType.REPAYMENT,
)


data class CampaignTransactionViewDto(
    val transactionId: Long,
    val transactionType: TransactionType,
    val pledgeTransactionType: PledgeTransactionType,
    val amount: BigDecimal,
    val createdAt: String,
)


data class CampaignTransactionHistoryResponse(
    val pledgeTransactions: List<CampaignTransactionViewDto>,
    val repaymentSummaries: List<MonthlyRepaymentSummaryDto>
)