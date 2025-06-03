package com.coded.loanlift.screens.campaigns.general

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.campaigns.CampaignExploreCard
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard
import com.coded.loanlift.composables.dashboard.SectionLoading
import com.coded.loanlift.composables.ui.SearchTopBarWithToggle
import com.coded.loanlift.composables.ui.SortAndFilterRow
import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.repositories.CategoryRepository
import com.coded.loanlift.viewModels.DashboardViewModel
import com.coded.loanlift.viewModels.PublicCampaignsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllPublicActiveCampaignsScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit,
    onCampaignClick: (Long) -> Unit
) {
    val uiState: PublicCampaignsUiState by viewModel.publicCampaignsUiState.collectAsState()
    val categories = CategoryRepository.categories

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<CampaignStatus?>(null) }
    var sortOption by remember { mutableStateOf("None") }
    var showSearchBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.fetchPublicCampaigns()
    }

    Scaffold(
        containerColor = Color(0xFF1A1B1E),
        topBar = {
            SearchTopBarWithToggle(
                title = "Explore Campaigns",
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                showSearchBar = showSearchBar,
                onToggleSearch = { showSearchBar = !showSearchBar },
                onBackClick = onBackClick
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
                onSortSelected = { sortOption = it },
                filterLabel = "Filter Status",
                filterOptions = listOf(null) + CampaignStatus.entries,
                selectedFilter = selectedStatus,
                onFilterSelected = { selectedStatus = it },
                filterLabelMapper = { it?.name ?: "All" }
            )

            when (val state = uiState) {
                is PublicCampaignsUiState.Loading -> {
                    SectionLoading(
                        sectionTitle = "Loading Campaigns",
                        onLinkClick = { }
                    ) {
                        SkeletonCampaignCard()
                    }
                }

                is PublicCampaignsUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        item() {
                            Text(text = "Campaigns")
                        }

                        item {
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
                                onSortSelected = { sortOption = it },
                                filterLabel = "Filter Status",
                                filterOptions = listOf(null) + CampaignStatus.entries,
                                selectedFilter = selectedStatus,
                                onFilterSelected = { selectedStatus = it },
                                filterLabelMapper = { it?.name ?: "All" }
                            )
                        }

                        items(
                            state.campaigns
                                .filter { it.title.contains(searchQuery, ignoreCase = true) }
                                .filter { selectedStatus == null || it.status == selectedStatus }
                                .let {
                                    when (sortOption) {
                                        "Name" -> it.sortedBy { c -> c.title }
                                        "Status" -> it.sortedBy { c -> c.status.name }
                                        "Goal Amount" -> it.sortedByDescending { c -> c.goalAmount }
                                        "Amount Raised" -> it.sortedByDescending { c -> c.amountRaised }
                                        "Deadline" -> it.sortedBy { c -> c.campaignDeadline }
                                        else -> it
                                    }
                                }) { campaign ->
                            CampaignExploreCard(
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth(),
                                campaign = campaign,
                                category = categories.find { it.id == campaign.categoryId },
                                onCardClick = {
                                    onCampaignClick(campaign.id)
                                },
                            ) {
                                Spacer(modifier = Modifier)
                            }
                        }
                    }
                }

                is PublicCampaignsUiState.Error -> {
                    Text(
                        text = "Failed to load campaigns: ${state.message}",
                        color = Color.Red,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}