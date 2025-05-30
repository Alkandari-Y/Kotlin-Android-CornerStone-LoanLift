package com.coded.loanlift.composables.campaigns

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.coded.loanlift.R
import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.response.campaigns.CampaignListItemResponse
import com.coded.loanlift.data.response.category.CategoryDto
import java.math.BigDecimal


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CampaignCard(
    modifier: Modifier = Modifier,
    campaign: CampaignListItemResponse,
    category: CategoryDto?,
    onCardClick: () -> Unit
) {
    val fundingProgress =
        if (campaign.goalAmount > BigDecimal.ZERO)
            (campaign.amountRaised / campaign.goalAmount).toFloat().coerceIn(0f, 1f)
        else 0f

    val showDeadline = campaign.status == CampaignStatus.ACTIVE

    val imagePainter = rememberAsyncImagePainter(
        model = campaign.imageUrl ?: R.drawable.default_campaign_image,
        placeholder = painterResource(R.drawable.default_campaign_image),
        error = painterResource(R.drawable.default_campaign_image)
    )

    Card(
        onClick = onCardClick,
        modifier = modifier
            .padding(bottom = 16.dp)
            .heightIn(min = 260.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B2E))
    ) {
        Column {
            BoxWithConstraints {
                Image(
                    painter = imagePainter,
                    contentDescription = "Campaign image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentScale = ContentScale.Crop
                )

                Text(
                    text = category?.name ?: "Uncategorized",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color(0x99000000), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = campaign.title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { fundingProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color(0xFF4B8BFF),
                    trackColor = Color(0xFF404040)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${(fundingProgress * 100).toInt()}% funded",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    if (showDeadline) {
                        Text(
                            text = "Ends: ${campaign.campaignDeadline}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Status: ${campaign.status.name}",
                    fontSize = 12.sp,
                    color = when (campaign.status) {
                        CampaignStatus.ACTIVE -> Color(0xFF4CAF50)
                        CampaignStatus.FUNDED -> Color(0xFF2196F3)
                        CampaignStatus.FAILED -> Color.Red
                        else -> Color.Gray
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}