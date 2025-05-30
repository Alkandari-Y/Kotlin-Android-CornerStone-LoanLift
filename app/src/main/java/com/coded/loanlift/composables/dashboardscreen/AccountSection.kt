package com.coded.loanlift.composables.dashboardscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coded.loanlift.composables.accounts.AccountCard
import com.coded.loanlift.data.response.accounts.AccountResponse

@Composable
fun AccountsSection(accounts: List<AccountResponse>) {
    Column {
        Text(
            text = "My Accounts",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            accounts.take(2).forEach { account ->
                AccountCard(
                    modifier = Modifier.weight(1f),
                    title = account.name,
                    accountNumber = account.accountNumber,
                    balance = "${account.balance} KWD"
                )
            }
        }
    }
}


