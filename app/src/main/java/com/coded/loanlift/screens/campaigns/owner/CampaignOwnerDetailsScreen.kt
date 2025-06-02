package com.coded.loanlift.screens.campaigns.owner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.campaignOwnerDetails.CampaignDetailsForOwner
import com.coded.loanlift.composables.campaignOwnerDetails.CampaignGeneralInfoCardOwner
import com.coded.loanlift.composables.campaignOwnerDetails.CampaignPledgeTransactionCard
import com.coded.loanlift.composables.campaignOwnerDetails.CampaignTransactionsTabSelector
import com.coded.loanlift.composables.campaignOwnerDetails.MonthlyRepaymentSummaryCard
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard
import com.coded.loanlift.data.enums.CampaignDetailsTab
import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.navigation.NavRoutes
import com.coded.loanlift.repositories.AccountRepository
import com.coded.loanlift.viewModels.CampaignDetailUiState
import com.coded.loanlift.viewModels.CampaignHistoryUiState
import com.coded.loanlift.viewModels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignOwnerDetailsScreen(
    navController: NavHostController,
    campaignId: Long,
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val campaignDetailUiState by viewModel.campaignDetailUiState.collectAsState()
    val campaignHistoryUiState by viewModel.campaignHistoryUiState.collectAsState()

    val campaign = (campaignDetailUiState as? CampaignDetailUiState.Success)?.campaign
    val campaignAccount = remember(campaign) {
        AccountRepository.myAccounts.find { it.id == campaign?.accountId }
    }

    var menuExpanded by remember { mutableStateOf(false) }

    var selectedTab by remember { mutableStateOf<CampaignDetailsTab?>(null) }

    LaunchedEffect(campaign?.status) {
        selectedTab = when (campaign?.status) {
            CampaignStatus.ACTIVE -> CampaignDetailsTab.PLEDGES
            CampaignStatus.FUNDED, CampaignStatus.COMPLETED, CampaignStatus.DEFAULTED -> CampaignDetailsTab.REPAYMENTS
            else -> CampaignDetailsTab.INFO
        }
    }


    LaunchedEffect(campaignId) {
        viewModel.fetchCampaignDetail(campaignId)
        viewModel.fetchCampaignTransactionHistory(campaignId)
    }

    Scaffold(
        containerColor = Color(0xFF1A1B1E),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Campaign",
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
                actions = {
                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {

                            DropdownMenuItem(
                                text = { Text("View Account", color = Color.Red) },
                                onClick = {
                                    menuExpanded = false
                                    campaignAccount?.accountNumber?.let { accountNum ->
                                        navController.navigate(
                                            NavRoutes.accountDetailRoute(
                                                accountNum
                                            )
                                        )
                                    }
                                }
                            )

                            if (campaign?.status == CampaignStatus.NEW) {
                                DropdownMenuItem(
                                    text = { Text("Delete", color = Color.Red) },
                                    onClick = {
                                        menuExpanded = false
                                        viewModel.deleteCampaign(campaign.id)
                                        onBackClick()
                                    }
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2A2B2E)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            item {
                when (val state = campaignDetailUiState) {
                    is CampaignDetailUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.Magenta)
                        }
                    }

                    is CampaignDetailUiState.Success -> {
                        CampaignDetailsForOwner(
                            campaign = state.campaign,
                        )
                    }

                    is CampaignDetailUiState.Error -> {
                        Text(
                            text = "Error loading campaign: ${state.message}",
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }

            item {
                if (campaign != null) {
                    selectedTab?.let { it ->
                        CampaignTransactionsTabSelector(
                            selectedTab = it,
                            onTabSelected = { selectedTab = it },
                        )
                    }
                }
            }


            when (campaignHistoryUiState) {
                is CampaignHistoryUiState.Success -> {
                    val history =
                        (campaignHistoryUiState as CampaignHistoryUiState.Success).campaignTransactionHistory
                    when (selectedTab) {
                        CampaignDetailsTab.INFO -> {
                            item {
                                CampaignGeneralInfoCardOwner(campaign)
                            }
                        }

                        CampaignDetailsTab.PLEDGES -> {
                            items(history.pledgeTransactions) { tx ->
                                CampaignPledgeTransactionCard(tx)
                            }
                        }

                        CampaignDetailsTab.REPAYMENTS -> {
                            items(history.repaymentSummaries) { summary ->
                                MonthlyRepaymentSummaryCard(summary)
                            }
                        }

                        null -> item {
                            Text(
                                text = when (campaign?.status) {
                                    CampaignStatus.NEW -> "Thank you for submitting your campaign. You may edit or remove your campaign application."
                                    CampaignStatus.PENDING -> "Campaign is under review."
                                    CampaignStatus.REJECTED -> "Campaign has been rejected."
                                    else -> "No data to display."
                                },
                                color = Color.Gray,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                is CampaignHistoryUiState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            SkeletonCampaignCard()
                        }
                    }
                }

                is CampaignHistoryUiState.Error -> {
                    item {
                        Text(
                            text = "Failed to load transactions: ${(campaignHistoryUiState as CampaignHistoryUiState.Error).message}",
                            color = Color.Red,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

