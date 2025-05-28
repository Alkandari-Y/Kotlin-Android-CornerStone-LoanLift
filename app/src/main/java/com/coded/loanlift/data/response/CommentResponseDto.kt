package com.coded.loanlift.data.response

import java.time.LocalDateTime

data class CommentResponseDto(
    val id: Long,
    val campaignId: Long,
    val message: String,
    val createdBy: Long,
    val createdAt: LocalDateTime,
    val reply: ReplyDto? = null
)