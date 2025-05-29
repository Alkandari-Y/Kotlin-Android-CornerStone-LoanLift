package com.coded.loanlift.data.response.comments

import java.time.LocalDateTime

data class ReplyDto(
    val id: Long,
    val message: String,
    val createdAt: LocalDateTime
)
