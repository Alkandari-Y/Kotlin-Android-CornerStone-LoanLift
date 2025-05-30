package com.coded.loanlift.data.response.auth

data class RegisterCreateRequest(
    val username: String,
    val password: String,
    val email: String,
    val civilId: String,
)
