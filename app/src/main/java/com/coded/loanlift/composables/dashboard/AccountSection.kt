package com.coded.loanlift.composables.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.accounts.AccountCard
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.navigation.NavRoutes

@Composable
fun AccountsSection(
    accounts: List<AccountDto>,
    navController: NavHostController,
    onAccountClick: (String) -> Unit,
    onAccountCreateClick: () -> Unit,
    onTransferClick: (String) -> Unit
    listState: LazyListState
) {
    DashboardSection(
        sectionTitle = "My Accounts",
        onLinkClick = { /* TODO */ }
    ) {
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(accounts) { account ->
                AccountCard(
                    modifier = Modifier.width(280.dp),
                    account = account,
                    onCardClick = { onAccountClick(account.accountNumber) },
                    onTransferClick = { onTransferClick(account.accountNumber) }
                )
            }

            item {
                CreateEntityCard(
                    modifier = Modifier.width(280.dp),
                    icon = Icons.Filled.CreditCard,
                    title = "Create An Account",
                    buttonText = "Get Started",
                    onCreateClick = onAccountCreateClick
                )
            }
        }
    }
}
