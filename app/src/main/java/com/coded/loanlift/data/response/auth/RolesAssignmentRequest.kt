package com.coded.loanlift.data.response.auth


data class RolesAssignmentRequest (
    val roles: List<String> = emptyList()
)