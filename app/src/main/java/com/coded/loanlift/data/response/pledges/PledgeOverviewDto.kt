package com.coded.loanlift.data.response.pledges

import com.coded.loanlift.data.enums.PledgeStatus
import java.math.BigDecimal
import java.time.LocalDate

data class PledgeOverviewDto(
    val id: Long,
    val userId: Long,
    val accountId: Long,
    val amount: BigDecimal,
    val createdAt: String,
    val updatedAt: String,
    val status: PledgeStatus,
    val withdrawnAt: String?,
)

