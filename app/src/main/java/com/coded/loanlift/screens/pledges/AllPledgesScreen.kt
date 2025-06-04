package com.coded.loanlift.screens.pledges

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.pledges.PledgeCard
import com.coded.loanlift.composables.pledges.PledgeListDisplay
import com.coded.loanlift.data.enums.PledgeStatus
import com.coded.loanlift.viewModels.DashboardViewModel
import com.coded.loanlift.viewModels.PledgesUiState

@Composable
fun AllPledgesScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit,
    onPledgeClick: (Long) -> Unit,
) {
    val uiState by viewModel.pledgesUiState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var sortOption by remember { mutableStateOf("None") }
    var showSearchBar by remember { mutableStateOf(false) }
    var isSortDescending by remember { mutableStateOf(true) }
    var selectedStatus by remember { mutableStateOf<PledgeStatus?>(null) }

    val filteredPledges = when (val state = uiState) {
        is PledgesUiState.Success -> {
            val filtered = state.pledges
                .filter { it.campaignTitle.contains(searchQuery, ignoreCase = true) }
                .filter { selectedStatus == null || it.status == selectedStatus }

            val sorted = when (sortOption) {
                "Amount" -> filtered.sortedBy { it.amount }
                "Status" -> filtered.sortedBy { it.status.name }
                "Campaign" -> filtered.sortedBy { it.campaignTitle }
                else -> filtered
            }

            if (isSortDescending) sorted.reversed() else sorted
        }

        else -> emptyList()
    }

    PledgeListDisplay(
        title = "All Pledges",
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        showSearchBar = showSearchBar,
        onToggleSearch = { showSearchBar = !showSearchBar },
        sortOption = sortOption,
        sortOptions = listOf("None", "Status", "Campaign"),
        onSortSelected = { sortOption = it },
        isSortDescending = isSortDescending,
        onToggleSortDirection = { isSortDescending = !isSortDescending },
        filterOptions = PledgeStatus.entries,
        selectedFilter = selectedStatus,
        onFilterSelected = { selectedStatus = it },
        filterLabel = "Status",
        sortLabel = "Sort By",
        isLoading = uiState is PledgesUiState.Loading,
        errorMessage = (uiState as? PledgesUiState.Error)?.message,
        onRefresh = { viewModel.fetchPledges() },
        onBackClick = onBackClick,
        renderCard = { pledge ->
            PledgeCard(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                pledge = pledge,
                onCardClick = { onPledgeClick(pledge.id) }
            )
        },
        pledges = filteredPledges
    )
}
