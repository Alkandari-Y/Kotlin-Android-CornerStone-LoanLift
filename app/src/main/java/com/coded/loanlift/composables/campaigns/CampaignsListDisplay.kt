package com.coded.loanlift.composables.campaigns

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.dashboard.SectionLoading
import com.coded.loanlift.composables.ui.SearchTopBarWithToggle
import com.coded.loanlift.composables.ui.SortAndFilterRow
import com.coded.loanlift.data.enums.CampaignStatus

@Composable
fun <T> CampaignsListDisplay(
    title: String,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    showSearchBar: Boolean,
    onToggleSearch: () -> Unit,
    sortOption: String,
    onSortSelected: (String) -> Unit,
    selectedStatus: CampaignStatus?,
    onStatusSelected: (CampaignStatus?) -> Unit,
    campaigns: List<T>,
    onBackClick: () -> Unit,
    renderCard: @Composable (T) -> Unit,
    isLoading: Boolean,
    errorMessage: String? = null,
    isSortDescending: Boolean,
    onToggleSortDirection: () -> Unit,
) {
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
                sortOptions = listOf(
                    "None",
                    "Name",
                    "Status",
                    "Goal Amount",
                    "Amount Raised",
                    "Deadline"
                ),
                selectedSort = sortOption,
                onSortSelected = onSortSelected,
                filterLabel = "Filter Status",
                filterOptions = listOf(null) + CampaignStatus.entries,
                selectedFilter = selectedStatus,
                onFilterSelected = onStatusSelected,
                filterLabelMapper = { it?.name ?: "All" },
                isSortDescending = isSortDescending,
                onToggleSortDirection = onToggleSortDirection
            )

            when {
                isLoading -> {
                    SectionLoading("Loading Campaigns") {
                        SkeletonCampaignCard()
                    }
                }

                errorMessage != null -> {
                    Text(
                        text = "Failed to load campaigns: $errorMessage",
                        color = Color.Red,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(campaigns) { campaign ->
                            renderCard(campaign)
                            Spacer(modifier = Modifier)
                        }
                    }
                }
            }
        }
    }
}
