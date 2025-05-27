package com.coded.loanlift.data.response

import java.math.BigDecimal

data class AccountListItemProjection (
    val id: Long,
    val name: String,
    val accountNumber: String,
    val balance: BigDecimal,
    val active: Boolean,
    val ownerId: Long,
    val isPrimary: Boolean,
)

data class AccountView (
    val id: Long,
    val accountNumber: String,
    val name: String,
    val balance: BigDecimal,
    val isActive: Boolean,
    val ownerId: Long,
    val ownerType: AccountType,
)
