package com.coded.loanlift.data.response


data class TransactionResponse(
    val sourceAccount: AccountEntity,
    val destinationAccount: AccountEntity,
    val transaction: TransactionEntity,
    val category: String
)

enum class TransactionType {
    TRANSFER,
    PLEDGE,
    REPAYMENT,
    REFUND,
    PAYMENT
}