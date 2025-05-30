package com.coded.loanlift.composables.campaigns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.ui.PlaceholderBox

@Composable
fun SkeletonCampaignCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(280.dp)
            .heightIn(min = 260.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B2E))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.DarkGray)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                PlaceholderBox(height = 18.dp, widthFraction = 0.7f)
                Spacer(modifier = Modifier.height(12.dp))

                PlaceholderBox(height = 12.dp, widthFraction = 0.4f)
                Spacer(modifier = Modifier.height(12.dp))

                PlaceholderBox(height = 4.dp, widthFraction = 1f) // progress bar
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PlaceholderBox(height = 12.dp, widthFraction = 0.3f)
                    PlaceholderBox(height = 12.dp, widthFraction = 0.3f)
                }

                Spacer(modifier = Modifier.height(10.dp))

                PlaceholderBox(height = 12.dp, widthFraction = 0.5f)
            }
        }
    }
}
