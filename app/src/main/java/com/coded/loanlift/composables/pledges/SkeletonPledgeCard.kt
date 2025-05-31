package com.coded.loanlift.composables.pledges

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.ui.PlaceholderBox

@Composable
fun SkeletonPledgeCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.heightIn(max = 180.dp).width(280.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B2E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                    .background(Color.DarkGray)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    PlaceholderBox(height = 16.dp, widthFraction = 0.6f)
                    Spacer(modifier = Modifier.height(8.dp))
                    PlaceholderBox(height = 14.dp, widthFraction = 0.5f)
                    Spacer(modifier = Modifier.height(4.dp))
                    PlaceholderBox(height = 14.dp, widthFraction = 0.4f)
                    Spacer(modifier = Modifier.height(8.dp))
                    PlaceholderBox(height = 12.dp, widthFraction = 0.5f)
                }

                Column {
                    PlaceholderBox(height = 12.dp, widthFraction = 0.5f)
                    Spacer(modifier = Modifier.height(4.dp))
                    PlaceholderBox(height = 12.dp, widthFraction = 0.4f)
                }
            }
        }
    }
}
