package com.coded.loanlift.screens.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.accounts.AccountCard
import com.coded.loanlift.composables.accounts.AccountsListDisplay
import com.coded.loanlift.composables.accounts.SkeletonAccountCard
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard
import com.coded.loanlift.composables.dashboard.SectionLoading
import com.coded.loanlift.composables.ui.SearchTopBarWithToggle
import com.coded.loanlift.composables.ui.SortAndFilterRow
import com.coded.loanlift.data.enums.AccountType
import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.viewModels.AccountsUiState
import com.coded.loanlift.viewModels.DashboardViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AllAccountsScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit,
    onAccountClick: (String) -> Unit
) {
    val uiState by viewModel.accountsUiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf("None") }
    var showSearchBar by remember { mutableStateOf(false) }
    var isSortDescending by remember { mutableStateOf(true) }
    var selectedOwnerType by remember { mutableStateOf<AccountType?>(null) }

    val filteredAccounts = when (val state = uiState) {
        is AccountsUiState.Success -> {
            val filtered = state.accounts
                .filter { it.name.contains(searchQuery, ignoreCase = true) }
                .filter { selectedOwnerType == null || it.ownerType == selectedOwnerType }

            val sorted = when (sortOption) {
                "Name" -> filtered.sortedBy { it.name }
                "Balance" -> filtered.sortedBy { it.balance }
                "Owner Type" -> filtered.sortedBy { it.ownerType.name }
                else -> filtered
            }

            if (isSortDescending) sorted.reversed() else sorted
        }

        else -> emptyList()
    }

    AccountsListDisplay(
        title = "All Accounts",
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        showSearchBar = showSearchBar,
        onToggleSearch = { showSearchBar = !showSearchBar },
        sortOption = sortOption,
        onSortSelected = { sortOption = it },
        isSortDescending = isSortDescending,
        onToggleSortDirection = { isSortDescending = !isSortDescending },
        accounts = filteredAccounts,
        onBackClick = onBackClick,
        isLoading = uiState is AccountsUiState.Loading,
        errorMessage = (uiState as? AccountsUiState.Error)?.message,
        onRefresh = { viewModel.fetchAccounts() },
        selectedOwnerType = selectedOwnerType,
        onOwnerTypeSelected = { selectedOwnerType = it },
        renderCard = { account ->
            AccountCard(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                account = account,
                onCardClick = { onAccountClick(account.accountNumber) },
                onTransferClick = {
                    navController.navigate("transfer/${account.accountNumber}")
                }
            )
        }
    )
}