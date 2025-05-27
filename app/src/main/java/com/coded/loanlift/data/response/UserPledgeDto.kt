
package com.joincoded.bankapi.data.response


import com.project.campaignlift.entities.PledgeEntity
import com.project.campaignlift.entities.PledgeStatus
import java.math.BigDecimal
import java.time.LocalDate

data class UserPledgeDto(
    val id: Long,
    val amount: BigDecimal,
    val status: PledgeStatus,
    val campaignTitle: String,
    val campaignId: Long,
    val createdAt: LocalDate
)