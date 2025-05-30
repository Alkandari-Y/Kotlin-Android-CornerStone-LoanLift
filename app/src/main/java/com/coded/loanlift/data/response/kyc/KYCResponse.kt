package com.coded.loanlift.data.response.kyc

import java.math.BigDecimal
import java.time.LocalDate

data class KYCResponse(
    val id: Long,
    val userId: Long,
    val firstName: String,
    val lastName: String,
    var dateOfBirth: String? = null,
    val salary: BigDecimal,
    val nationality: String
)
