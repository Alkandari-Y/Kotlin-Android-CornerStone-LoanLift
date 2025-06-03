package com.coded.loanlift.composables.campaigns

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
fun CampaignPublicInformation(
    campaign: CampaignOwnerDetails
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B2E)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = campaign.description,
                fontSize = 14.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Investment Details", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color.White)

            Spacer(modifier = Modifier.height(8.dp))

            DetailRow("Goal", "\$${campaign.goalAmount}")
            DetailRow("Interest Rate", "${campaign.interestRate}%")
            DetailRow("Repayment Period", "${campaign.repaymentMonths} months")
        }
    }
}