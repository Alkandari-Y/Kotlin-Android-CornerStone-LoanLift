package com.coded.loanlift.data.response

data class ReplyCreateRequest(
    val commentId: Long,
    val message: String,
)