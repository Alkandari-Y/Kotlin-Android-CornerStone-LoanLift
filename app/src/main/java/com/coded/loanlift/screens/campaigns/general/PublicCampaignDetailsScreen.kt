package com.coded.loanlift.screens.campaigns.general

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.coded.loanlift.composables.campaigns.CampaignExploreCard
import com.coded.loanlift.composables.campaigns.CampaignPublicInformation
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard
import com.coded.loanlift.composables.comments.CampaignCommentsSection
import com.coded.loanlift.composables.comments.CommentInputOverlay
import com.coded.loanlift.data.response.campaigns.toCampaignListItemResponse
import com.coded.loanlift.data.response.comments.CommentResponseDto
import com.coded.loanlift.repositories.CategoryRepository
import com.coded.loanlift.repositories.UserRepository
import com.coded.loanlift.viewModels.*
import com.coded.loanlift.formStates.comments.CommentFormState

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
    val pledgesUiState by viewModel.pledgesUiState.collectAsState()
    val postCommentUiState by viewModel.postCommentsUiState.collectAsState()
    val postReplyUiState by viewModel.postReplyUiState.collectAsState()


    val categories = CategoryRepository.categories
    val listState = rememberLazyListState()
    var isWritingComment by remember { mutableStateOf(false) }
    var commentFormState by remember { mutableStateOf(CommentFormState()) }
    var replyToComment by remember { mutableStateOf<CommentResponseDto?>(null) }
    var replyText by remember { mutableStateOf("") }




    val currentUserId = UserRepository.userInfo?.userId

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
                        CampaignCommentsSection(
                            campaignId = campaign.id,
                            campaignCreatorId = campaign.createdBy,
                            viewModel = viewModel,
                            isWritingComment = isWritingComment,
                            updateWritingCommentState = { isWritingComment = !isWritingComment },
                            onTriggerReply = {
                                replyToComment = it
                                isWritingComment = true
                            }
                        )
                    }
                }
            }

            is CampaignDetailUiState.Error -> {
                Text(
                    text = "Error loading campaign: ${state.message}",
                    color = Color.Red,
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        if (isWritingComment || replyToComment != null) {
            val isPostingComment = postCommentUiState is PostCommentUiState.Loading
            val isPostingReply = postReplyUiState is PostReplyUiState.Loading
            CommentInputOverlay(
                isReply = replyToComment != null,
                initialText = replyText,
                commentFormState = commentFormState,
                updateCommentFormState = { commentFormState = it },
                onCancel = {
                    isWritingComment = false
                    replyToComment = null
                    replyText = ""
                    commentFormState = CommentFormState()
                },
                onSubmit = { message ->
                    if (replyToComment != null) {
                        viewModel.postReply(replyToComment!!.id, message)
                        replyToComment = null
                    } else {
                        viewModel.postComment(campaignId, CommentFormState(message = message))
                        commentFormState = CommentFormState()
                    }
                    isWritingComment = false
                    replyText = ""
                    viewModel.fetchCampaignComments(campaignId)
                },
                isSubmitting = if (replyToComment != null) isPostingReply else isPostingComment
            )
        }
    }
}
