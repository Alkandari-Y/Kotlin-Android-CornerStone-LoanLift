package com.coded.loanlift.composables.comments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coded.loanlift.data.response.comments.CommentResponseDto
import com.coded.loanlift.repositories.UserRepository
import com.coded.loanlift.viewModels.CommentsUiState
import com.coded.loanlift.viewModels.DashboardViewModel
import com.coded.loanlift.viewModels.PostCommentUiState
import com.coded.loanlift.viewModels.PostReplyUiState

@Composable
fun CampaignCommentsSection(
    campaignId: Long,
    campaignCreatorId: Long,
    viewModel: DashboardViewModel,
    isWritingComment: Boolean,
    updateWritingCommentState: () -> Unit,
    onTriggerReply: (CommentResponseDto?) -> Unit
) {
    val commentsUiState by viewModel.commentsUiState.collectAsState()
    val postCommentUiState by viewModel.postCommentsUiState.collectAsState()
    val postReplyUiState by viewModel.postReplyUiState.collectAsState()
    val currentUserId = UserRepository.userInfo?.userId

    LaunchedEffect(campaignId) {
        viewModel.fetchCampaignComments(campaignId)
    }

    Column(modifier = Modifier.padding(10.dp)) {
        Text("Comments", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        if (postCommentUiState is PostCommentUiState.Loading) {
            SkeletonCommentCard()
        }

        when (val commentState = commentsUiState) {
            is CommentsUiState.Loading -> {
                Text("Loading comments...", color = Color.Gray)
            }
            is CommentsUiState.Success -> {
                commentState.comments.forEach { comment ->
                    CommentCard(
                        comment = comment,
                        currentUserIsCampaignOwner = currentUserId == campaignCreatorId,
                        onReplyClick = {
                            onTriggerReply(it)
                            updateWritingCommentState()
                        },
                        showReplySkeleton = postReplyUiState is PostReplyUiState.Loading,
                        updateWritingCommentState = {updateWritingCommentState()}
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
            is CommentsUiState.Error -> {
                Text("Failed to load comments: ${commentState.message}", color = Color.Red)
            }
        }
    }
}
