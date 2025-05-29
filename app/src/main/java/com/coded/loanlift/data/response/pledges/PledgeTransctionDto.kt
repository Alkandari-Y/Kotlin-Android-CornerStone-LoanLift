package com.coded.loanlift.data.response.pledges

import com.coded.loanlift.data.enums.PledgeTransactionType


data class PledgeTransactionDto(
    val id: Long,
    val transactionId: Long,
    val type: PledgeTransactionType
)
