package com.coded.loanlift.data.response.accounts


data class UpdateAccountRequest(
    val name: String,
    val asPrimary: Boolean?
)
