package com.coded.loanlift.composables.campaignOwnerDetails

import androidx.compose.foundation.layout.Column
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
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails


@Composable
fun CampaignGeneralInfoCardOwner(campaign: CampaignOwnerDetails?) {
    if (campaign == null) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B2E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Campaign Title", fontSize = 12.sp, color = Color.Gray)
            Text(campaign.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Description", fontSize = 12.sp, color = Color.Gray)
            Text(campaign.description, fontSize = 14.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Goal Amount", fontSize = 12.sp, color = Color.Gray)
            Text("${campaign.goalAmount} KWD", fontSize = 14.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Interest Rate", fontSize = 12.sp, color = Color.Gray)
            Text("${campaign.interestRate}%", fontSize = 14.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Repayment Months", fontSize = 12.sp, color = Color.Gray)
            Text("${campaign.repaymentMonths}", fontSize = 14.sp, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Monthly Installment", fontSize = 12.sp, color = Color.Gray)
            Text("${campaign.monthlyInstallment} KWD", fontSize = 14.sp, color = Color.White)
        }
    }
}
