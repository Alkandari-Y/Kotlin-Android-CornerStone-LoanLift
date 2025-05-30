package com.coded.loanlift.data.response.accounts

import com.coded.loanlift.data.enums.AccountType
import java.math.BigDecimal

data class AccountDto(
    val id: Long,
    val accountNumber: String,
    val name: String,
    val balance: BigDecimal,
    val active: Boolean,
    val ownerId: Long,
    val ownerType: AccountType
)