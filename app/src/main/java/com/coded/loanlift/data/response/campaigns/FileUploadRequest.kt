package com.coded.loanlift.data.response.campaigns


data class FileUploadRequest(
    val campaignId: Long,
    val isPublic: Boolean = false
)