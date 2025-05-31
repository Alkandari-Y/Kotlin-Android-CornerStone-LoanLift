package com.coded.loanlift.data.response.comments

import java.time.LocalDateTime

data class CommentResponseDto(
    val id: Long,
    val campaignId: Long,
    val message: String,
    val createdBy: Long,
    val createdAt: String,
    val reply: ReplyDto? = null
)