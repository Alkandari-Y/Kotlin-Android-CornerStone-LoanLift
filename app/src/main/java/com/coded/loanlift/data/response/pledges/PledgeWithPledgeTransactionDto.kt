package com.coded.loanlift.data.response.pledges

import com.coded.loanlift.data.enums.PledgeStatus
import java.math.BigDecimal

data class PledgeWithPledgeTransactionsDto(
    val id: Long,
    val campaignId: Long,
    val userId: Long,
    val accountId: Long,
    val amount: BigDecimal,
    val status: PledgeStatus,
    val transactions: List<PledgeTransactionDto>
)
