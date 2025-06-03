package com.coded.loanlift.screens.campaigns.owner

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.campaigns.CampaignCard
import com.coded.loanlift.composables.campaigns.CampaignsListDisplay
import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.repositories.CategoryRepository
import com.coded.loanlift.viewModels.CampaignsUiState
import com.coded.loanlift.viewModels.DashboardViewModel


@Composable
fun AllCampaignsOwnerScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit,
    onCampaignClick: (Long) -> Unit
) {
    val uiState: CampaignsUiState by viewModel.campaignsUiState.collectAsState()
    val categories = CategoryRepository.categories

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<CampaignStatus?>(null) }
    var sortOption by remember { mutableStateOf("None") }
    var showSearchBar by remember { mutableStateOf(false) }
    var isSortDescending by remember { mutableStateOf(true) }

    val filteredOwnerCampaigns = when (val state = uiState) {
        is CampaignsUiState.Success -> {
            val filtered = state.campaigns
                .filter { it.title.contains(searchQuery, ignoreCase = true) }
                .filter { selectedStatus == null || it.status == selectedStatus }

            val sorted = when (sortOption) {
                "Name" -> filtered.sortedBy { it.title }
                "Status" -> filtered.sortedBy { it.status.name }
                "Goal Amount" -> filtered.sortedBy { it.goalAmount }
                "Amount Raised" -> filtered.sortedBy { it.amountRaised }
                "Deadline" -> filtered.sortedBy { it.campaignDeadline }
                else -> filtered
            }

            if (isSortDescending) sorted.reversed() else sorted
        }

        else -> emptyList()
    }


    CampaignsListDisplay(
        title = "Manage Campaigns",
        searchQuery = searchQuery,
        onSearchQueryChange = { searchQuery = it },
        showSearchBar = showSearchBar,
        onToggleSearch = { showSearchBar = !showSearchBar },
        sortOption = sortOption,
        onSortSelected = { sortOption = it },
        selectedStatus = selectedStatus,
        onStatusSelected = { selectedStatus = it },
        campaigns = filteredOwnerCampaigns,
        onBackClick = onBackClick,
        isLoading = uiState is CampaignsUiState.Loading,
        errorMessage = (uiState as? CampaignsUiState.Error)?.message,
        renderCard = { campaign ->
            CampaignCard(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                campaign = campaign,
                category = categories.find { it.id == campaign.categoryId },
                onCardClick = { onCampaignClick(campaign.id) }
            )
        },
        isSortDescending = isSortDescending,
        onToggleSortDirection = { isSortDescending = !isSortDescending },
    )
}