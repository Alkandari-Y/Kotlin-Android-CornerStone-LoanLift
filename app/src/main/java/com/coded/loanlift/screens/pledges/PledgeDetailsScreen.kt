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
import com.coded.loanlift.viewModels.PledgeDetailsViewModel
import com.coded.loanlift.viewModels.PledgeTransationsUiState

@Composable
fun PledgeDetailsScreen(
    pledgeId: Long,
    viewModel: PledgeDetailsViewModel = viewModel()
){
    val uiState by viewModel.pledgesUiState.collectAsState()
    val shouldNavigate by viewModel.shouldNavigate.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadPledgeTransactions(pledgeId)

    }

    LaunchedEffect(shouldNavigate){
        if (shouldNavigate){
            viewModel.resetNavigationFlag()
        }
    }

    LazyColumn {
        when (val state = uiState){
            is PledgeTransationsUiState.Loading -> {
                item {
                    Box(modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }

            }
            is  PledgeTransationsUiState.Success -> {
                items(state.pledgeTransactions) { pledge ->
                    Column (modifier = Modifier.padding(16.dp)){
                        Text("Pledge ID: ${pledge}")
                        Text("Status: ${pledge.transactionType}")
                        Text("Amount: ${pledge.amount}")
                        Text("Account ID: ${pledge.type}")

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Transactions:", fontWeight = FontWeight.Bold)
                    }
                }
            }

            is PledgeTransationsUiState.Error -> {
                item {
                    Text(text = "Error")
                }
            }
            null -> item{ Text(text = "Error") }
        }
    }

}




//                        pledge.transactions.forEach {tx ->
//                            Card (
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 4.dp),
//                                elevation = CardDefaults.cardElevation()
//                            ){
//                                Column (modifier = Modifier.padding(8.dp)){
//                                    Text("Tranaction ID: ${tx.transactionId}")
//                                    Text("Type: ${tx.type}")
//                                    Text("")
//                                }
//
//                            }
//                        }