package com.coded.loanlift.screens.campaigns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coded.loanlift.composables.dashboard.AccountsSection
import com.coded.loanlift.composables.dashboard.AccountsSectionLoading
import com.coded.loanlift.data.enums.AccountType
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.data.response.auth.JwtResponse
import com.coded.loanlift.data.response.auth.UserInfoDto
import com.coded.loanlift.data.response.auth.ValidateTokenResponse
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails
import com.coded.loanlift.repositories.UserRepository
import com.coded.loanlift.screens.accounts.AccountDetailsScreen
import com.coded.loanlift.screens.accounts.TransactionsHeader
import com.coded.loanlift.viewModels.AccountsUiState
import com.coded.loanlift.viewModels.CampaignDetailUiState
import com.coded.loanlift.viewModels.CampaignOwnerViewModel
import java.math.BigDecimal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignOwnerDetailsScreen(
    campaignId: Long,
    viewModel: CampaignOwnerViewModel = viewModel(),
    onBackClick: () -> Unit
) {

    val context = LocalContext.current
    val campaignDetailUiState by viewModel.campaignDetailUiState.collectAsState()
    val campaignHistoryUiState by viewModel.campaignHistoryUiState.collectAsState()


    LaunchedEffect(campaignId) {
        viewModel.fetchCampaignDetail(campaignId)
        viewModel.fetchCampaignTransactionHistory(campaignId)
    }

    val pledges = listOf(
        "Ahmadi Ali - 1,000 KD",
        "Younna Salim - 2,500 KD",
        "Sarah AlEnzi - 500 KD",
        "Yousef AlShemmari - 3,450 KD",
        "Yara Ahmad - 1,000 KD"
    )

    LazyColumn {
        item {
            when (val state = campaignDetailUiState) {
                is CampaignDetailUiState.Loading -> CircularProgressIndicator()
                is CampaignDetailUiState.Success -> CampaignDetailsForOwner(
                    campaign = state.campaign,
                    userInfo = UserRepository.userInfo!!
                )
                is CampaignDetailUiState.Error -> Text(
                    text = "Failed to load accounts: ${state.message}",
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }
        }

        item { TransactionsHeader() }

        items(pledges) { donor ->
            PledgeViewCard(donor)
        }
    }
}

@Composable
fun CampaignDetailsForOwner(
    userInfo: ValidateTokenResponse,
    campaign: CampaignOwnerDetails
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B2541))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = campaign.title, fontWeight = FontWeight.Bold,
                fontSize = 24.sp, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            Text("75%", color = Color.Magenta, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Text("Funding - in progress", color = Color.White)
        }
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
