package com.coded.loanlift.data.response


import java.math.BigDecimal

data class AccountCreateRequest(
    val initialBalance: BigDecimal,
    val name: String
)

enum class AccountType {
    USER, CAMPAIGN
}