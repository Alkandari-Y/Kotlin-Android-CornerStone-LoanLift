package com.coded.loanlift.screens.home
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import com.coded.loanlift.repositories.UserRepository
import com.coded.loanlift.viewModels.*

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    navController: NavHostController,
    onLogoutClick: () -> Unit,
    onAccountClick: (String) -> Unit,
    onCampaignClick: (Long) -> Unit,
    onPledgeCLick: (Long) -> Unit,
    onAccountCreateClick: () -> Unit,
    onCampaignCreateClick: () -> Unit,
    onPledgeCreateClick: () -> Unit,
    onProfileClick: () -> Unit,
    onViewAllCampaignsClick: () -> Unit
) {
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

        LazyColumn(verticalArrangement = Arrangement.spacedBy(15.dp)) {
            item {
                Spacer(modifier = Modifier.padding(10.dp))
            }

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
                        listState = lazyListStatePledges

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
                    onClick = onViewAllCampaignsClick,
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
