package com.coded.loanlift.screens.pledges

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.coded.loanlift.R
import com.coded.loanlift.viewModels.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PledgeDetailsScreen(
    pledgeId: Long,
    navController: NavController,
    viewModel: PledgeDetailsViewModel = viewModel()
) {
    val uiDetailsState by viewModel.pledgeDetailsUiState.collectAsState()
    val uiTransactionsUiState by viewModel.pledgesUiState.collectAsState()
    val campaignState by viewModel.campaignDetailsUiState.collectAsState()
    val shouldNavigate by viewModel.shouldNavigate.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val darkBackground = Color(0xFF1A1B1E)
    val cardBackground = Color(0xFF2A2B2E)
    val headerColor = Color.White
    val subTextColor = Color(0xFFB0BEC5)

    var showPledgeInfo by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var newAmount by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }

    val isCampaignFundable = (campaignState as? CampaignDetailsUiState.Success)?.campaignDetails?.status ==
            com.coded.loanlift.data.enums.CampaignStatus.ACTIVE

    LaunchedEffect(pledgeId) {
        viewModel.loadPledgeDetails(pledgeId)
        viewModel.loadPledgeTransactions(pledgeId)
    }

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            viewModel.resetNavigationFlag()
        }
    }

    Scaffold(
        containerColor = darkBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Pledge Details", color = headerColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = headerColor)
                    }
                },
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = Color.White)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                        DropdownMenuItem(
//                            text = { Text("Update Pledge") },
//                            onClick = {
//                                if (isCampaignFundable) {
//                                    showUpdateDialog = true
//                                } else {
//                                    coroutineScope.launch {
//                                        snackbarHostState.showSnackbar("Cannot update: campaign is not funding.")
//                                    }
//                                }
//                                expanded = false
//                            }
//                        )
                        DropdownMenuItem(
                            text = { Text("Withdraw Pledge") },
                            onClick = {
                                if (isCampaignFundable) {
                                    viewModel.withdrawPledge(pledgeId)
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Cannot withdraw: campaign is not funding.")
                                    }
                                }
                                expanded = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cardBackground)
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (val pledgeState = uiDetailsState) {
                is PledgeDetailsUiState.Loading -> item { CenteredLoading() }
                is PledgeDetailsUiState.Success -> {
                    when (val campaignStateVal = campaignState) {
                        is CampaignDetailsUiState.Loading -> item { CenteredLoading() }
                        is CampaignDetailsUiState.Success -> item {
                            val campaign = campaignStateVal.campaignDetails
                            val imageUrl = campaign.imageUrl?.replace("localhost", "10.0.2.2")?.let { "$it?ext=.jpg" }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = cardBackground)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    campaign.imageUrl?.let {
                                        AsyncImage(
                                            model = imageUrl,
                                            contentDescription = "Campaign image",
                                            contentScale = ContentScale.FillWidth,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(180.dp),
                                            placeholder = painterResource(R.drawable.default_campaign_image),
                                            error = painterResource(R.drawable.default_campaign_image),
                                            fallback = painterResource(R.drawable.default_campaign_image)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(campaign.title, color = headerColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                        Text(
                                            text = if (showPledgeInfo) "Campaign Info" else "Pledge Info",
                                            color = Color(0xFF90CAF9),
                                            fontSize = 14.sp,
                                            modifier = Modifier.clickable { showPledgeInfo = !showPledgeInfo }
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    if (showPledgeInfo) {
                                        PledgeDetailRow("Amount", "${pledgeState.pledgeDetails.amount} KWD")
                                        PledgeDetailRow("Status", pledgeState.pledgeDetails.status.name)
                                        PledgeDetailRow("Interest Rate", "${pledgeState.pledgeDetails.interestRate}%")
                                        PledgeDetailRow("Campaign Status", pledgeState.pledgeDetails.campaignStatus.name)
                                    } else {
                                        PledgeDetailRow("Description", campaign.description)
                                        PledgeDetailRow("Goal", "${campaign.goalAmount} KWD")
                                        PledgeDetailRow("Deadline", campaign.campaignDeadline)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Tap to view campaign →",
                                            color = Color(0xFF90CAF9),
                                            fontSize = 14.sp,
                                            modifier = Modifier.clickable {
                                                navController.navigate("campaigns/explore/${campaign.id}")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        is CampaignDetailsUiState.Error -> item { ErrorMessage("Error loading campaign info") }
                    }
                }
                is PledgeDetailsUiState.Error -> item { ErrorMessage("Error loading pledge details") }
            }

            item {
                Text("Transactions", color = headerColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            when (val state = uiTransactionsUiState) {
                is PledgeTransactionsUiState.Loading -> item { CenteredLoading() }
                is PledgeTransactionsUiState.Success -> {
                    items(state.pledgeTransactions) { tx ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBackground)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("${tx.type.name} - ${tx.amount} KWD", color = headerColor, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Type: ${tx.transactionType.name}", color = subTextColor, fontSize = 13.sp)
                                Text("Created At: ${tx.createdAt}", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }
                }
                is PledgeTransactionsUiState.Error -> item {
                    ErrorMessage("Error loading transactions")
                }
            }
        }
    }

    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        newAmount.toBigDecimalOrNull()?.let {
                            viewModel.updatePledge(pledgeId, it)
                            showUpdateDialog = false
                        }
                    },
                    enabled = newAmount.toBigDecimalOrNull() != null
                ) { Text("Update") }
            },
            dismissButton = {
                Button(onClick = { showUpdateDialog = false }) { Text("Cancel") }
            },
            title = { Text("Update Pledge", color = Color.White) },
            text = {
                OutlinedTextField(
                    value = newAmount,
                    onValueChange = { newAmount = it },
                    label = { Text("New Amount") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.Gray,
                        focusedContainerColor = Color(0xFF2A2B2E),
                        unfocusedContainerColor = Color(0xFF2A2B2E)
                    )
                )
            },
            containerColor = Color(0xFF1A1B1E)
        )
    }
}

@Composable
fun CenteredLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun ErrorMessage(message: String) {
    Text(message, color = Color.Red, fontSize = 14.sp)
}

@Composable
fun PledgeDetailRow(label: String, value: String) {
    Column(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(text = label, color = Color(0xFFB0BEC5), fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}
