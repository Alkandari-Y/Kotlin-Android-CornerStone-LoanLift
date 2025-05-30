package com.coded.loanlift.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coded.loanlift.data.enums.AccountType
import com.coded.loanlift.data.response.accounts.AccountDto
import java.math.BigDecimal


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    onBackClick: () -> Unit,
    onCampaignClick: (String) -> Unit
) {
    val darkBlue = Color(0xFF1B2541)
    val navyBlue = Color(0xFF1F2937)
    val account =  AccountDto (
        id = 1L,
        accountNumber = "1111",
        name =  "Meshal Alquraini",
        balance = BigDecimal("3000"),
        active = true,
        ownerId = 1,
        ownerType = AccountType.CAMPAIGN
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Account Details", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle menu click */ }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = darkBlue)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(
                        text = account.name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Account Number",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = account.accountNumber,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Account Type",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = account.ownerType.toString(),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Current Balance",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "${account.balance} KWD",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                TransactionsHeader()
                TransactionsList(onCampaignClick)

            }
        }
    }
}

@Composable
fun TransactionsHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1B2541))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Transactions History", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Row {
            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            Spacer(modifier = Modifier.width(12.dp))
        }
    }
}

@Composable
fun TransactionsList(onCampaignClick: (String) -> Unit) {
    val transactions = listOf(
        "Pledge for Progress",
        "Commit to Win",
        "Promise for the Planet",
        "Fuel the Future",
        "Mission: Possible",
        "sourceAccount: true"
    )

    LazyColumn {
        items(transactions) { campaign ->
            TransactionCard(campaign = campaign, onClick = { onCampaignClick(campaign) })
        }
    }
}

@Composable
fun TransactionCard(campaign: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(Color(0xFF1B2541))
            .clickable { onClick() },
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF3F51B5))
    ) {
        // we can add if statement for the icon to show deposit or withdraw
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(Icons.Default.ArrowUpward, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Debit–Pledge", fontWeight = FontWeight.Bold, color = Color.White)
                Text("\"$campaign\" - campaign", color = Color.White)

            }
        }
    }
}
