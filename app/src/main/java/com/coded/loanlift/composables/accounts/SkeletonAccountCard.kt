package com.coded.loanlift.composables.accounts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.coded.loanlift.composables.ui.PlaceholderBox

@Composable
fun SkeletonAccountCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.heightIn(max = 180.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B2E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            PlaceholderBox(height = 16.dp, widthFraction = 0.6f)
            Spacer(modifier = Modifier.height(8.dp))

            PlaceholderBox(height = 12.dp, widthFraction = 0.4f)
            Spacer(modifier = Modifier.height(12.dp))

            PlaceholderBox(height = 12.dp, widthFraction = 0.3f)
            Spacer(modifier = Modifier.height(4.dp))

            PlaceholderBox(height = 18.dp, widthFraction = 0.5f)

            Spacer(modifier = Modifier.weight(1f))

            PlaceholderBox(height = 40.dp, widthFraction = 1f)
        }
    }
}
