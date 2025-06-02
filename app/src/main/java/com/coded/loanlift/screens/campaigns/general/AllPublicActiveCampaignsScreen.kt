package com.coded.loanlift.screens.campaigns.general

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

    LaunchedEffect(Unit) {
        viewModel.fetchPublicCampaigns()
    }

    Scaffold(
        containerColor = Color(0xFF1A1B1E),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Explore Campaigns",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2A2B2E)
                )
            )
        }
    ) { paddingValues ->
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

                    items(state.campaigns) { campaign ->
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