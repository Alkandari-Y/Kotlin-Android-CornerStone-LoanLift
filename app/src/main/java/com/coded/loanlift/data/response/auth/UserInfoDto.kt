package com.coded.loanlift.data.response.auth

data class UserInfoDto(
    val userId: Long,
    val isActive: Boolean,
    val email: String,
    val username: String
)
