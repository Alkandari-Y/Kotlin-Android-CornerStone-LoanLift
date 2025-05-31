
package com.coded.loanlift.data.response.pledges

import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.enums.PledgeStatus
import java.math.BigDecimal
import java.time.LocalDate

data class UserPledgeDto(
    val id: Long,
    val amount: BigDecimal,
    val status: PledgeStatus,
    val campaignTitle: String,
    val campaignId: Long,
    val createdAt: String,
    val campaignImage: String,
    val interestRate: BigDecimal,
    val campaignStatus: CampaignStatus,
)