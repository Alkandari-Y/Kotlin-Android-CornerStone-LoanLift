package com.coded.loanlift.composables.pledges

import androidx.compose.ui.draw.clip
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.coded.loanlift.R
import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.enums.PledgeStatus
import com.coded.loanlift.data.response.pledges.UserPledgeDto

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun PledgeCard(
    modifier: Modifier = Modifier,
    pledge: UserPledgeDto,
    onCardClick: () -> Unit
) {
    val imageUrl = pledge.campaignImage
        ?.replace("localhost", "10.0.2.2")
        ?.let { "$it?ext=.jpg" } ?: ""

    val campaignStatusColor = when (pledge.campaignStatus) {
        CampaignStatus.ACTIVE -> Color(0xFF2196F3)
        CampaignStatus.FUNDED -> Color(0xFF4CAF50)
        CampaignStatus.FAILED -> Color.Red
        else -> Color.Gray
    }

    Card(
        onClick = onCardClick,
        modifier = modifier.heightIn(max = 180.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B2E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
            ) {
                AsyncImage(
                    model = imageUrl,
                    placeholder = painterResource(R.drawable.default_campaign_image),
                    error = painterResource(R.drawable.default_campaign_image),
                    fallback = painterResource(R.drawable.default_campaign_image),
                    contentDescription = "Campaign Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
                )

                Text(
                    text = pledge.campaignStatus.name,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .background(campaignStatusColor.copy(alpha = 0.8f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight()
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = pledge.campaignTitle,
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${pledge.amount} KWD",
                        color = Color(0xFFB0BEC5),
                        fontSize = 14.sp
                    )

                    Text(
                        text = "${pledge.interestRate.toFloat() * 100}%",                        color = Color(0xFFB0BEC5),
                        fontSize = 14.sp
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Status:",
                        color = Color(0xFFB0BEC5),
                        fontSize = 12.sp
                    )
                    Text(
                        text = pledge.status.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (pledge.status) {
                            PledgeStatus.COMMITTED -> Color(0xFF81C784)
                            PledgeStatus.WITHDRAWN -> Color(0xFFE57373)
                        }
                    )
                }
            }
        }
    }
}