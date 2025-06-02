package com.coded.loanlift.screens.accounts

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coded.loanlift.data.enums.AccountType
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.viewModels.DashboardViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(
    onBackClick: () -> Unit,
    onCampaignClick: (String) -> Unit,
    viewModel: DashboardViewModel
) {
    val darkBlue = Color(0xFF1B2541)
    val account = AccountDto(
        id = 1L,
        accountNumber = "1111",
        name = "Meshal Alquraini",
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )

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
        }
    }
}
