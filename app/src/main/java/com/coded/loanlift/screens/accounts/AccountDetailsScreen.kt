package com.coded.loanlift.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coded.loanlift.data.enums.AccountType
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.utils.formatDateTime
import com.coded.loanlift.viewModels.DashboardViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    onBackClick: () -> Unit,
    onCampaignClick: (String) -> Unit,
    viewModel: DashboardViewModel,
    accountNumber: String
) {
    val darkBlue = Color(0xFF1B2541)
    val accountState by viewModel.selectedAccount.collectAsState()
    val transactionsState by viewModel.accountTransactions.collectAsState()

    LaunchedEffect(accountNumber) {
        viewModel.fetchAccountDetails(accountNumber)
        viewModel.fetchAccountTransactions(accountNumber)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF1A1B1E)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text("Account Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A2B2E))
            )

            when (val account = accountState) {
                null -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                else -> {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = darkBlue)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(account.name, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Account Number", color = Color.White, fontSize = 16.sp)
                            Text(account.accountNumber, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Account Type", color = Color.White, fontSize = 16.sp)
                            Text(account.ownerType.toString(), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                            Spacer(modifier = Modifier.height(16.dp))

                            Text("Current Balance", color = Color.White, fontSize = 16.sp)
                            Text("${account.balance} KWD", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text("Transactions", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(16.dp))

                    when (val txs = transactionsState) {
                        null -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                        else -> LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                            items(txs) { tx ->
                                val isSource = account.accountNumber == tx.sourceAccountNumber
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row() {
                                            Text(text = if (isSource) {
                                                "To: ${tx.destinationAccountNumber}"
                                            } else {
                                                "From: ${tx.sourceAccountNumber}"
                                            },
                                            color = Color.White
                                            )
                                        }

                                        Row() {
                                            Icon(
                                                imageVector = if (isSource) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                                                contentDescription = "",
                                                tint = if (isSource) Color.Red else Color.Green,
//                                                modifier = Modifier.size(36.dp)
                                            )
                                            Text("${tx.amount} KWD", color = Color.White)

                                        }
                                        Text("Date: ${formatDateTime(tx.createdAt)}", color = Color.White)
                                        Text("Category: ${tx.category}", color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}