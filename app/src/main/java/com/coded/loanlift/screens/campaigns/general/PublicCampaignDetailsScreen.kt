package com.coded.loanlift.screens.campaigns.general

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.campaigns.CampaignExploreCard
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails
import com.coded.loanlift.data.response.campaigns.toCampaignListItemResponse
import com.coded.loanlift.data.response.comments.CommentResponseDto
import com.coded.loanlift.repositories.CategoryRepository
import com.coded.loanlift.viewModels.CampaignDetailUiState
import com.coded.loanlift.viewModels.CommentsUiState
import com.coded.loanlift.viewModels.DashboardViewModel
import com.coded.loanlift.viewModels.PledgesUiState
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.coded.loanlift.composables.campaigns.CampaignPublicInformation
import com.coded.loanlift.composables.comments.CommentCard
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PublicCampaignDetailsScreen(
    viewModel: DashboardViewModel,
    campaignId: Long,
    navController: NavHostController,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current

    val campaignDetailUiState by viewModel.campaignDetailUiState.collectAsState()
    val commentsUiState by viewModel.commentsUiState.collectAsState()
    val pledgesUiState by viewModel.pledgesUiState.collectAsState()

    val categories = CategoryRepository.categories

    val listState = rememberLazyListState()
    var isWritingComment by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }

    var replyToComment by remember { mutableStateOf<CommentResponseDto?>(null) }
    var replyText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchCampaignDetail(campaignId)
        viewModel.fetchCampaignComments(campaignId)
        viewModel.fetchPledges()
    }

    Scaffold(
        containerColor = Color(0xFF1A1B1E),
        topBar = {
            TopAppBar(
                title = {
                    Text("Campaign", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A2B2E))
            )
        },
        floatingActionButton = {
            if (!isWritingComment) {
                Button(
                    onClick = {
                        isWritingComment = true
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)),
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text("Comment")
                }
            }
        }
    ) { paddingValues ->
        when (val state = campaignDetailUiState) {
            is CampaignDetailUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    SkeletonCampaignCard()
                }
            }

            is CampaignDetailUiState.Success -> {
                val campaign = state.campaign
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        CampaignExploreCard(
                            campaign = campaign.toCampaignListItemResponse(),
                            onCardClick = {},
                            category = categories.find { it.id == campaign.categoryId },
                            modifier = Modifier
                                .fillMaxWidth(),
                            composable = {}
                        )
                    }

                    item {
                        when (pledgesUiState) {
                            is PledgesUiState.Success -> {
                                val pledges = (pledgesUiState as PledgesUiState.Success).pledges
                                val hasPledged = pledges.any { it.campaignId == campaign.id }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (hasPledged) {
                                        Button(
                                            onClick = { /* TODO  */},
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF6200EE)
                                            ),
                                        ) {
                                            Text("View Your Pledge")
                                        }
                                    } else {
                                        Button(
                                            onClick = { /* TODO  */},
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF6200EE)
                                            ),
                                        ) {
                                            Text("Pledge Now")
                                        }
                                    }
                                }
                            }

                            is PledgesUiState.Loading -> {
                                Text(
                                    text = "Checking your pledge status...",
                                    color = Color.Gray,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }

                            is PledgesUiState.Error -> {
                                Text(
                                    text = "Couldn't verify pledge status",
                                    color = Color.Red,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                        CampaignPublicInformation(campaign)
                    }

                    item {
                        Text(
                            text = "Comments",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White,
                        )
                    }

                    when (val commentState = commentsUiState) {
                        is CommentsUiState.Loading -> {
                            item { Text("Loading comments...", color = Color.Gray) }
                        }

                        is CommentsUiState.Success -> {
                            items(commentState.comments.sortedByDescending { LocalDateTime.parse(it.createdAt) }) { comment ->
                                CommentCard(
                                    comment = comment,
                                    onReplyClick = { selectedComment ->
                                        replyToComment = selectedComment
                                        isWritingComment = true
                                    }
                                )
                            }
                        }

                        is CommentsUiState.Error -> {
                            item {
                                Text("Failed to load comments: ${commentState.message}", color = Color.Red)
                            }
                        }
                    }
                }
            }

            is CampaignDetailUiState.Error -> {
                Text(
                    text = "Error loading campaign: ${state.message}",
                    color = Color.Red,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
        if (isWritingComment || replyToComment != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A2B2E), shape = RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    OutlinedTextField(
                        value = if (replyToComment != null) replyText else commentText,
                        onValueChange = { newValue ->
                            if (replyToComment != null) replyText = newValue else commentText = newValue
                        },
                        placeholder = {
                            Text(if (replyToComment != null) "Write a reply..." else "Write a comment...")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF2A2B2E),
                            unfocusedContainerColor = Color(0xFF2A2B2E)
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                                isWritingComment = false
                                commentText = ""
                                replyToComment = null
                                replyText = ""
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (replyToComment != null) {
                                    viewModel.postReply(replyToComment!!.id, replyText)
                                    replyToComment = null
                                    replyText = ""
                                } else {
                                    viewModel.postComment(campaignId, commentText)
                                    commentText = ""
                                }
                                isWritingComment = false
                                viewModel.fetchCampaignComments(campaignId)
                            },
                            enabled = if (replyToComment != null) replyText.isNotBlank() else commentText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }
}
