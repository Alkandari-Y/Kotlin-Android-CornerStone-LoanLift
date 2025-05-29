package com.coded.loanlift.data.response.comments

data class ReplyCreateRequest(
    val commentId: Long,
    val message: String,
)