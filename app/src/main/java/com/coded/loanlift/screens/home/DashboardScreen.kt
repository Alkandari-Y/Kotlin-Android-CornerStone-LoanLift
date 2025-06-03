package com.coded.loanlift.screens.home
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.dashboard.*
import com.coded.loanlift.composables.ui.TopBar
import com.coded.loanlift.managers.TokenManager
import com.coded.loanlift.navigation.NavRoutes
import com.coded.loanlift.viewModels.*
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    onCampaignClick: (Long) -> Unit,
    onPledgeCLick: (Long) -> Unit,
    onAccountCreateClick: () -> Unit,
    onCampaignCreateClick: () -> Unit,
    onPledgeCreateClick: () -> Unit,
    onAccountClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onViewAllCampaignsClick: () -> Unit,
    onExploreAllCampaignsClick: () -> Unit,
    onViewAllAccounts: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullToRefreshState = rememberPullToRefreshState()

    fun refreshAllData() {
        isRefreshing = true
        coroutineScope.launch {
            viewModel.fetchAccounts()
            viewModel.fetchCampaigns()
            viewModel.fetchPledges()
            delay(1000)
            isRefreshing = false
        }
    }

    val context = LocalContext.current
    val accountsUiState by viewModel.accountsUiState.collectAsState()
    val campaignsUiState by viewModel.campaignsUiState.collectAsState()
    val pledgesUiState by viewModel.pledgesUiState.collectAsState()

    val lazyListStateAccounts = rememberLazyListState()
    val lazyListStateCampaigns = rememberLazyListState()
    val lazyListStatePledges = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1B1E))
            .padding(16.dp)
    ) {
        TopBar(
            onProfileClick = onProfileClick,
            onLogoutClick = {
                TokenManager.clearToken(context)
                onLogoutClick()
            }
        )

        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = { refreshAllData() },
            state = pullToRefreshState,
            indicator = {
                PullToRefreshDefaults.Indicator(
                    modifier = Modifier
                        .align(Alignment.TopCenter),
                    isRefreshing = isRefreshing,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    state = pullToRefreshState
                )
            }
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
                item { Spacer(modifier = Modifier.padding(10.dp)) }

                item {
                    when (val state = accountsUiState) {
                        is AccountsUiState.Loading -> AccountsSectionLoading()
                        is AccountsUiState.Success -> AccountsSection(
                            accounts = state.accounts,
                            navController = navController,
                            onAccountClick = onAccountClick,
                            onAccountCreateClick = onAccountCreateClick,
                            onTransferClick = { accountNum ->
                                navController.navigate(NavRoutes.transferRoute(accountNum))
                            },
                            onViewAllClick = onViewAllAccounts,
                            listState = lazyListStateAccounts
                        )

                        is AccountsUiState.Error -> Text(
                            text = "Failed to load accounts: ${state.message}",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    when (val state = campaignsUiState) {
                        is CampaignsUiState.Loading -> CampaignsSectionLoading()
                        is CampaignsUiState.Success -> CampaignsSection(
                            campaigns = state.campaigns,
                            navController = navController,
                            onCampaignClick = onCampaignClick,
                            onCampaignCreateClick = onCampaignCreateClick,
                            onViewAllClick = onViewAllCampaignsClick,
                            listState = lazyListStateCampaigns
                        )

                        is CampaignsUiState.Error -> Text(
                            text = "Failed to load campaigns: ${state.message}",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    when (val state = pledgesUiState) {
                        is PledgesUiState.Loading -> PledgesSectionLoading()
                        is PledgesUiState.Success -> PledgesSection(
                            pledges = state.pledges,
                            onPledgeClick = onPledgeCLick,
                            listState = lazyListStatePledges,
                            onPledgeCreate = onPledgeCreateClick
                        )

                        is PledgesUiState.Error -> Text(
                            text = "Failed to load pledges: ${state.message}",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                item {
                    Button(
                        onClick = onExploreAllCampaignsClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(
                            text = "Explore Campaigns",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}