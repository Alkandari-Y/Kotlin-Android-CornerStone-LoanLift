package com.coded.loanlift.data.response


data class FileUploadRequest(
    val campaignId: Long,
    val isPublic: Boolean = false
)