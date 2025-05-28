package com.coded.loanlift.data.response

import java.math.BigDecimal

data class PledgeCreateRequest(
    val accountId: Long,
    val campaignId: Long,
    val amount: BigDecimal
)

