package com.coded.loanlift.data.response


data class PledgeTransactionDto(
    val id: Long,
    val transactionId: Long,
    val type: PledgeTransactionType
)
