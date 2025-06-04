package com.coded.loanlift.composables.pledges

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.ui.SearchTopBarWithToggle
import com.coded.loanlift.composables.ui.SortAndFilterRow
import com.coded.loanlift.data.enums.PledgeStatus
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PledgeListDisplay(
    title: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    showSearchBar: Boolean,
    onToggleSearch: () -> Unit,
    sortOption: String,
    sortOptions: List<String>,
    onSortSelected: (String) -> Unit,
    isSortDescending: Boolean,
    onToggleSortDirection: () -> Unit,
    filterOptions: List<PledgeStatus>,
    selectedFilter: PledgeStatus?,
    onFilterSelected: (PledgeStatus?) -> Unit,
    filterLabel: String,
    sortLabel: String,
    isLoading: Boolean,
    errorMessage: String? = null,
    onRefresh: () -> Unit,
    onBackClick: () -> Unit,
    pledges: List<UserPledgeDto>,
    renderCard: @Composable (UserPledgeDto) -> Unit,
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
                sortLabel = sortLabel,
                sortOptions = sortOptions,
                selectedSort = sortOption,
                onSortSelected = onSortSelected,
                filterLabel = filterLabel,
                filterOptions = listOf(null) + filterOptions,
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected,
                filterLabelMapper = { it?.name ?: "All" },
                isSortDescending = isSortDescending,
                onToggleSortDirection = onToggleSortDirection
            )

            when {
                isLoading -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(3) {
                            Text(
                                text = "Loading...",
                                color = Color.White,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }

                errorMessage != null -> {
                    Text(
                        text = "Failed to load pledges: $errorMessage",
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
                            items(pledges) { pledge ->
                                renderCard(pledge)
                            }
                        }
                    }
                }
            }
        }
    }
}
