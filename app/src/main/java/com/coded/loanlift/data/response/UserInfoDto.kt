package com.coded.loanlift.data.response

data class UserInfoDto(
    val userId: Long,
    val isActive: Boolean,
    val email: String,
    val username: String
)
