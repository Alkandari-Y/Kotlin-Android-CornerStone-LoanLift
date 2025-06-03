package com.coded.loanlift.composables.pledges

import androidx.compose.ui.draw.clip
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        .replace("localhost", "10.0.2.2")
        .let { "$it?ext=.jpg" }

    Card(
        modifier = modifier.heightIn(max = 180.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2B2E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = imageUrl,
                placeholder = painterResource(R.drawable.default_campaign_image),
                error = painterResource(R.drawable.default_campaign_image),
                fallback = painterResource(R.drawable.default_campaign_image),
                contentDescription = "Campaign Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp))
            )

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

                    Text(
                        text = "Pledged: ${pledge.amount} KWD",
                        color = Color(0xFFB0BEC5),
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Interest: ${pledge.interestRate}%",
                        color = Color(0xFFB0BEC5),
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Created at: ${pledge.createdAt}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }

                Column {
                    Text(
                        text = "Pledge: ${pledge.status.name}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = when (pledge.status) {
                            PledgeStatus.COMMITTED -> Color(0xFF2196F3)
                            PledgeStatus.WITHDRAWN -> Color.Red
                        }
                    )

                    Text(
                        text = "Campaign: ${pledge.campaignStatus}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
