package com.coded.loanlift.data.response

import java.math.BigDecimal

data class TransferCreateRequest(
    val sourceAccountNumber: String,
    val destinationAccountNumber: String,
    val amount: BigDecimal,
    val type: TransactionType?
)
