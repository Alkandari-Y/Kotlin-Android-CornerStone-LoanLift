package com.coded.loanlift.screens.campaigns

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.campaigns.CampaignCard
import com.coded.loanlift.composables.dashboard.AccountsSection
import com.coded.loanlift.composables.dashboard.AccountsSectionLoading
import com.coded.loanlift.data.enums.AccountType
import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.data.response.auth.JwtResponse
import com.coded.loanlift.data.response.auth.UserInfoDto
import com.coded.loanlift.data.response.auth.ValidateTokenResponse
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails
import com.coded.loanlift.data.response.campaigns.toCampaignListItemResponse
import com.coded.loanlift.data.response.category.CategoryDto
import com.coded.loanlift.navigation.NavRoutes
import com.coded.loanlift.repositories.AccountRepository
import com.coded.loanlift.repositories.CategoryRepository
import com.coded.loanlift.repositories.UserRepository
import com.coded.loanlift.screens.accounts.AccountDetailsScreen
import com.coded.loanlift.screens.accounts.TransactionsHeader
import com.coded.loanlift.viewModels.AccountsUiState
import com.coded.loanlift.viewModels.CampaignDetailUiState
import com.coded.loanlift.viewModels.DashboardViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignOwnerDetailsScreen(
    navController: NavHostController,
    campaignId: Long,
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val campaignDetailUiState by viewModel.campaignDetailUiState.collectAsState()
    val campaignHistoryUiState by viewModel.campaignHistoryUiState.collectAsState()

    val campaign = (campaignDetailUiState as? CampaignDetailUiState.Success)?.campaign
    val campaignAccount = remember(campaign) {
        AccountRepository.myAccounts.find { it.id == campaign?.accountId }
    }

    var menuExpanded by remember { mutableStateOf(false) }

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
                                        navController.navigate(NavRoutes.accountDetailRoute(accountNum))
                                    }
                                }
                            )

                            if (campaign?.status == CampaignStatus.NEW) {
                                DropdownMenuItem(
                                    text = { Text("Delete", color = Color.Red) },
                                    onClick = {
                                        menuExpanded = false
                                        // TODO: Delete
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
                            userInfo = UserRepository.userInfo!!
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

            item { TransactionsHeader() }

            items(
                listOf(
                    "Ahmadi Ali - 1,000 KD",
                    "Younna Salim - 2,500 KD",
                    "Sarah AlEnzi - 500 KD",
                    "Yousef AlShemmari - 3,450 KD",
                    "Yara Ahmad - 1,000 KD"
                )
            ) { donor ->
                PledgeViewCard(donor)
            }
        }
    }
}

@Composable
fun CampaignDetailsForOwner(
    userInfo: ValidateTokenResponse,
    campaign: CampaignOwnerDetails
) {

    CampaignCard(
        onCardClick = {},
        modifier = Modifier.fillMaxWidth(),
        campaign = campaign.toCampaignListItemResponse(),
        category = CategoryDto(campaign.categoryId, campaign.categoryName),
        contentScale = ContentScale.FillWidth,
        heightIn = 340.dp
    ) {
        Spacer(modifier = Modifier)

    }
}


@Composable
fun PledgeViewCard(donorInfo: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color(0xFF173E5D))
                .padding(16.dp)
        ) {
            Icon(Icons.Rounded.AccountBalanceWallet, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Credit–Pledge", fontWeight = FontWeight.Bold, color = Color.White)
                Text(donorInfo, color = Color.White)
            }
        }
    }
}
