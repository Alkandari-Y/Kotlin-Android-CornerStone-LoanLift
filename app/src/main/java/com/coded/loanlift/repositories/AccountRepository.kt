package com.coded.loanlift.repositories

import com.coded.loanlift.data.response.accounts.AccountDto

object AccountRepository {
    var myAccounts: MutableList<AccountDto> = mutableListOf()
}