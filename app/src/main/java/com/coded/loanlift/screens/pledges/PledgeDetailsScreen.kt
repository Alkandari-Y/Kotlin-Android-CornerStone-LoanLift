package com.coded.loanlift.screens.pledges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.coded.loanlift.viewModels.PledgeDetailsUiState
import com.coded.loanlift.viewModels.PledgeDetailsViewModel
import com.coded.loanlift.viewModels.PledgeTransactionsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PledgeDetailsScreen(
    pledgeId: Long,
    navController: NavController,
    viewModel: PledgeDetailsViewModel = viewModel()
) {
    val uiDetailsState by viewModel.pledgeDetailsUiState.collectAsState()
    val uiTransactionsUiState by viewModel.pledgesUiState.collectAsState()
    val shouldNavigate by viewModel.shouldNavigate.collectAsState()

    val darkBlue = Color(0xFF1B2541)

    LaunchedEffect(Unit) {
        viewModel.loadPledgeDetails(pledgeId)
        viewModel.loadPledgeTransactions(pledgeId)
    }

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate) {
            viewModel.resetNavigationFlag()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1A1B1E)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Pledge Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A2B2E))
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()

                    .padding(16.dp),

                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (val state = uiDetailsState) {
                    is PledgeDetailsUiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is PledgeDetailsUiState.Success -> {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = darkBlue),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {

                                    Text("Pledge Information", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text("Amount",color = Color.White, fontSize = 16.sp)
                                    Text("${state.pledgeDetails.amount}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                                    Text("Status", color = Color.White, fontSize = 16.sp)
                                    Text("${state.pledgeDetails.status}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                    Text("Interest Rate", color = Color.White, fontSize = 16.sp)
                                    Text("${state.pledgeDetails.interestRate}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    is PledgeDetailsUiState.Error -> {
                        item {
                            Text("Error loading pledge details", color = Color.Red)
                        }
                    }
                }

                item {
                    Text("Transactions", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(top = 8.dp))
                }

                when (val state = uiTransactionsUiState) {
                    is PledgeTransactionsUiState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is PledgeTransactionsUiState.Success -> {
                        items(state.pledgeTransactions) { tx ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Type: ${tx.type}", color = Color.White)
                                    Text("Amount: ${tx.amount}", color = Color.White)
                                    Text("Transaction Type: ${tx.transactionType}", color = Color.White)
                                    Text("Created At: ${tx.createdAt}", color = Color.White)
                                }
                            }
                        }
                    }

                    is PledgeTransactionsUiState.Error -> {
                        item {
                            Text("Error loading transactions", color = Color.Red)
                        }
                    }
                }
            }
        }
    }
}
