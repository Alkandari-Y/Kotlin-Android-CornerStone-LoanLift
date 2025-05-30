package com.coded.loanlift.data.response.pledges

import com.coded.loanlift.data.enums.PledgeTransactionType
import com.coded.loanlift.data.enums.TransactionType
import java.math.BigDecimal
import java.time.Instant

data class PledgeTransactionWithDetails(
    val id: Long?,
    val type: PledgeTransactionType,
    val transactionId: Long,
    val amount: BigDecimal,
    val transactionType: TransactionType,
    val createdAt: Instant
)


