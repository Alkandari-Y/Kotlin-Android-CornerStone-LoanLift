package com.coded.loanlift.data.response.auth

data class JwtResponse(val access: String, val refresh: String)

data class JwtContents (
    val userId: Long,
    val isActive: Boolean,
    val roles: List<String>,
    val type: String
)

data class ValidateTokenResponse (
    val userId: Long,
    val isActive: Boolean,
    val roles: List<String>,
    val email: String,
    val username: String
)