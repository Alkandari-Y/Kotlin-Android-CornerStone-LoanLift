package com.coded.loanlift.composables.dashboardscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.accounts.AccountCard
import com.coded.loanlift.composables.accounts.CreateAccountCard
import com.coded.loanlift.data.response.accounts.AccountDto

@Composable
fun AccountsSection(accounts: List<AccountDto>) {
    DashboardSection(
        sectionTitle = "My Accounts",
        onLinkClick = { /* TODO */ }
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(accounts) { account ->
                AccountCard(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(200.dp),
                    account = account,
                    onCardClick = { /* TODO */ },
                    onTransferClick = { /* TODO */ }
                )
            }

            item {
                CreateAccountCard(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(200.dp),
                    onCreateClick = { /* TODO */ }
                )
            }
        }
    }
}
