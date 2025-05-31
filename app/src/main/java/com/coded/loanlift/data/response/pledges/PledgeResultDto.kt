package com.coded.loanlift.data.response.pledges

import java.math.BigDecimal
import java.time.LocalDate

data class PledgeResultDto(
    val pledge: UserPledgeDto,
    val transaction: PledgeTransactionDto
)


data class UserPledgeProjection (
    val id: Long,
    val amount: BigDecimal,
    val status: Int,
    val campaignTitle: String,
    val campaignId: Long,
    val createdAt: String
)
