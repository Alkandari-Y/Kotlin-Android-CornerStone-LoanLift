package com.coded.loanlift.composables.accounts

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
import com.coded.loanlift.composables.ui.SearchTopBarWithToggle
import com.coded.loanlift.composables.ui.SortAndFilterRow
import com.coded.loanlift.data.enums.AccountType
import com.coded.loanlift.data.response.accounts.AccountDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountsListDisplay(
    title: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    showSearchBar: Boolean,
    onToggleSearch: () -> Unit,
    sortOption: String,
    onSortSelected: (String) -> Unit,
    isSortDescending: Boolean,
    onToggleSortDirection: () -> Unit,
    accounts: List<AccountDto>,
    onBackClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String? = null,
    onRefresh: () -> Unit,
    selectedOwnerType: AccountType?,
    onOwnerTypeSelected: (AccountType?) -> Unit,
    renderCard: @Composable (AccountDto) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    fun refreshAllData() {
        isRefreshing = true
        coroutineScope.launch {
            onRefresh()
            delay(1000)
            isRefreshing = false
        }
    }

    Scaffold(
        containerColor = Color(0xFF1A1B1E),
        topBar = {
            SearchTopBarWithToggle(
                title = title,
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                showSearchBar = showSearchBar,
                onToggleSearch = onToggleSearch,
                onBackClick = onBackClick,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SortAndFilterRow(
                sortLabel = "Sort By",
                sortOptions = listOf("None", "Name", "Balance", "Owner Type"),
                selectedSort = sortOption,
                onSortSelected = onSortSelected,
                filterLabel = "Filter",
                filterOptions = listOf(null) + AccountType.entries,
                selectedFilter = selectedOwnerType,
                onFilterSelected = onOwnerTypeSelected,
                filterLabelMapper = { it?.name ?: "All" },
                isSortDescending = isSortDescending,
                onToggleSortDirection = onToggleSortDirection
            )

            when {
                isLoading -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(2) { SkeletonAccountCard() }
                    }
                }

                errorMessage != null -> {
                    Text(
                        text = "Failed to load accounts: $errorMessage",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = { refreshAllData() },
                        state = pullToRefreshState,
                        indicator = {
                            PullToRefreshDefaults.Indicator(
                                modifier = Modifier.align(Alignment.TopCenter),
                                isRefreshing = isRefreshing,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                state = pullToRefreshState
                            )
                        }
                    ) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(accounts) { account ->
                                renderCard(account)
                            }
                        }
                    }
                }
            }
        }
    }
}