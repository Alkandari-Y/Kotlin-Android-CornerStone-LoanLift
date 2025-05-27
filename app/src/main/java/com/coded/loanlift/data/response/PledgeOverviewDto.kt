package com.coded.loanlift.data.response

import java.math.BigDecimal
import java.time.LocalDate

data class PledgeOverviewDto(
    val id: Long,
    val userId: Long,
    val accountId: Long,
    val amount: BigDecimal,
    val createdAt: LocalDate,
    val updatedAt: LocalDate,
    val status: PledgeStatus,
    val withdrawnAt: LocalDate?,
)

enum class PledgeStatus {
    COMMITTED,
    WITHDRAWN,
}
