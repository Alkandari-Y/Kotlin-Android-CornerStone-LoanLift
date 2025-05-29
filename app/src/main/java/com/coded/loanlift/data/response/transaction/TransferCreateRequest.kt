package com.coded.loanlift.data.response.transaction

import com.coded.loanlift.data.enums.TransactionType
import java.math.BigDecimal

data class TransferCreateRequest(
    val sourceAccountNumber: String,
    val destinationAccountNumber: String,
    val amount: BigDecimal,
    val type: TransactionType?
)
