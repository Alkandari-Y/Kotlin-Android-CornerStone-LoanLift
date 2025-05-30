package com.coded.loanlift.data.response.campaigns


import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.response.comments.ReplyDto
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

sealed interface CampaignDetailResponse

data class CampaignPublicDetails(
    val id: Long,
    val createdBy: Long,
    val categoryId: Long,
    val categoryName: String,
    val title: String,
    val description: String,
    val goalAmount: BigDecimal,
    val interestRate: BigDecimal,
    val repaymentMonths: Int,
    val status: CampaignStatus,
    val submittedAt: LocalDate,
    val campaignDeadline: LocalDate,
    val imageUrl: String?,
    var amountRaised: BigDecimal,
    var accountId: Long? = null,
)  : CampaignDetailResponse

data class CampaignPublicDetailsWithComments(
    val id: Long?,
    val createdBy: Long?,
    val categoryId: Long?,
    val categoryName: String,
    val title: String,
    val description: String?,
    val goalAmount: BigDecimal?,
    val interestRate: BigDecimal,
    val repaymentMonths: Int,
    val status: CampaignStatus,
    val submittedAt: LocalDate,
    val campaignDeadline: LocalDate?,
    val imageUrl: String?,
    val amountRaised: BigDecimal = BigDecimal.ZERO,
    var accountId: Long? = null,
    val comments: List<Comment> = emptyList()
) : CampaignDetailResponse

data class Comment(
    val id: Long,
    val category: Long,
    val message: String,
    val createdBy: Long,
    val createdAt: LocalDateTime,
    val reply: ReplyDto? = null
)