package com.coded.loanlift.data.response

import java.math.BigDecimal

data class KYCRequest(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String,
    val salary: BigDecimal,
    val nationality: String,
)