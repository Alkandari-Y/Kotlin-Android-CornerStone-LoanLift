package com.coded.loanlift.data.response


data class UpdateAccountRequest(
    val name: String,
    val asPrimary: Boolean?
)
