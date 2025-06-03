package com.coded.loanlift.screens.pledges

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.coded.loanlift.viewModels.PledgeDetailsUiState
import com.coded.loanlift.viewModels.PledgeDetailsViewModel
import com.coded.loanlift.viewModels.PledgeTransactionsUiState

@Composable
fun PledgeDetailsScreen(
    pledgeId: Long,
    viewModel: PledgeDetailsViewModel = viewModel()
){
    val uiDetailsState by viewModel.pledgeDetailsUiState.collectAsState()
    val uiTransactionsUiState by viewModel.pledgesUiState.collectAsState()
    val shouldNavigate by viewModel.shouldNavigate.collectAsState()

    //for pledge details
    LaunchedEffect (Unit){
        viewModel.loadPledgeDetails(pledgeId)
    }

    //for pledges transactions
    LaunchedEffect(Unit) {
        viewModel.loadPledgeTransactions(pledgeId)

    }

    LaunchedEffect(shouldNavigate){
        if (shouldNavigate){
            viewModel.resetNavigationFlag()
        }
    }

    LazyColumn {
        when (val state = uiDetailsState) {
            is PledgeDetailsUiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is PledgeDetailsUiState.Success -> {
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Amount: ${state.pledgeDetails.amount}")
                        Text("Pledge Status: ${state.pledgeDetails.status}")
                        Text("Campaign Title: ${state.pledgeDetails.campaignTitle}")
                        Text("Campaign ID: ${state.pledgeDetails.campaignId}")
                        Text("Campaign Status: ${state.pledgeDetails.campaignStatus}")
                        Text("Interest Rate: ${state.pledgeDetails.interestRate}")
                        Text("Created ${state.pledgeDetails.createdAt}")

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Transactions:", fontWeight = FontWeight.Bold)
                    }
                }
            }
            is PledgeDetailsUiState.Error -> {
                item {
                    Text("Error")
                }
            }
        }


        when (val state = uiTransactionsUiState) {
            is PledgeTransactionsUiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

            }

            is PledgeTransactionsUiState.Success -> {
                items(state.pledgeTransactions) { pledge ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Pledge ID: ${pledge}")
                        Text("Status: ${pledge.transactionType}")
                        Text("Amount: ${pledge.amount}")
                        Text("Account ID: ${pledge.type}")

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Transactions:", fontWeight = FontWeight.Bold)
                    }
                }
            }

            is PledgeTransactionsUiState.Error -> {
                item {
                    Text(text = "Error")
                }
            }
        }



    }
}