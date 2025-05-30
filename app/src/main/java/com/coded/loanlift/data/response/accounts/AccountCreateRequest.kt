package com.coded.loanlift.data.response.accounts


import java.math.BigDecimal

data class AccountCreateRequest(
    val initialBalance: BigDecimal,
    val name: String
)

