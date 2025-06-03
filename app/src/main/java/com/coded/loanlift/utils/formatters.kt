package com.coded.loanlift.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatDateTime(isoString: String): String {
    return try {
        val parsed = LocalDateTime.parse(isoString)
        parsed.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    } catch (e: Exception) {
        isoString
    }
}