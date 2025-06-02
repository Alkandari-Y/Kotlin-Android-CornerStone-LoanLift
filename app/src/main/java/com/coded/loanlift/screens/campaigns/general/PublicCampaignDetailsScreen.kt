package com.coded.loanlift.screens.campaigns.general

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.viewModels.AccountsUiState
import com.coded.loanlift.viewModels.CreatePledgeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
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
    val accountsState by viewModel.accountsUiState.collectAsState()
    var showPledgeDialog by remember { mutableStateOf(false) }
    var pledgeAccount by remember { mutableStateOf<AccountDto?>(null) }
    var pledgeAmount by remember { mutableStateOf("") }
    val uiState by viewModel.createPledgeUiState.collectAsState()
var isLoading by remember { mutableStateOf(false) }
    val campaignDetailUiState by viewModel.campaignDetailUiState.collectAsState()
    val commentsUiState by viewModel.commentsUiState.collectAsState()
    val pledgesUiState by viewModel.pledgesUiState.collectAsState()

    val categories = CategoryRepository.categories

    val listState = rememberLazyListState()
    var isWritingComment by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchCampaignDetail(campaignId)
        viewModel.fetchCampaignComments(campaignId)
        viewModel.fetchPledges()
        viewModel.fetchAccounts()
    }
    fun resetPledgeDialogState() {
        pledgeAmount = ""
        pledgeAccount = null
        viewModel.resetCreatePledgeState()
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
                                        val hasAccounts = accountsState is AccountsUiState.Success &&
                                                (accountsState as AccountsUiState.Success).accounts.isNotEmpty()

                                        Button(
                                            onClick = { showPledgeDialog = true },
                                            enabled = hasAccounts,
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (hasAccounts) Color(0xFF6200EE) else Color.Gray
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
                            items(commentState
                                .comments
                                .sortedByDescending { LocalDateTime.parse(it.createdAt) }
                            ) { comment ->
                                CommentCard(comment)
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
        if (isWritingComment) {
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
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Write a comment...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            cursorColor = Color.White,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
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
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                viewModel.postComment(campaignId, commentText)
                                isWritingComment = false
                                commentText = ""
                                viewModel.fetchCampaignComments(campaignId)
                            },
                            enabled = commentText.isNotBlank(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                        ) {
                            Text("Submit")
                        }
                    }
                }
            }
        }
    }

    if (showPledgeDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showPledgeDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.createPledge(
                            accountId = pledgeAccount!!.id,
                            campaignId = campaignId,
                            amount = pledgeAmount.toBigDecimalOrNull()!!,
                        )
                    },
                    enabled = pledgeAccount != null && pledgeAmount.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                ) {
                    Text("Submit")

                }
            },
            dismissButton = {
                Button(
                    onClick = { showPledgeDialog = false
                         pledgeAmount=""
                        pledgeAccount=null
                        resetPledgeDialogState()

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text("Cancel")
                }
            },
            title = { Text("Make a Pledge", fontWeight = FontWeight.Bold, color = Color.White) },
            text = {

                Column(modifier = Modifier.fillMaxWidth()) {
//                    if (isLoading){
//                        Box(
//                            modifier = Modifier.fillMaxSize(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            CircularProgressIndicator()
//                        }
//                    }

                    Text(
                        "Select Account",
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    when (accountsState) {
                        is AccountsUiState.Success -> {
                            val accounts = (accountsState as AccountsUiState.Success).accounts
                            var selectedIndex by rememberSaveable { mutableStateOf(0) }
                            pledgeAccount = accounts[selectedIndex]

                            DropdownList(
                                itemList = accounts,
                                selectedIndex = selectedIndex,
                                modifier = Modifier.fillMaxWidth()
                            ) { index ->
                                selectedIndex = index
                            }
                        }

                        is AccountsUiState.Loading -> {
                           TODO()
                        }

                        is AccountsUiState.Error -> {
                            TODO()
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Enter Amount",
                        color = Color.White,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    OutlinedTextField(
                        value = pledgeAmount,
                        onValueChange = { pledgeAmount = it },
                        placeholder = {Text("Amount") },

                                modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedContainerColor = Color(0xFF2A2B2E),
                            unfocusedContainerColor = Color(0xFF2A2B2E)
                        )
                    )
                    when (uiState) {
                        is CreatePledgeUiState.Loading -> {
                            isLoading = true
                        }
                        is CreatePledgeUiState.Success -> {
                            // show success message or navigate
                            Text(text = "Pledge successful!", color = Color.Green)

                            LaunchedEffect(Unit) {
                                delay(1000)
                                showPledgeDialog = false
                                resetPledgeDialogState()

                            }
                        }
                        is CreatePledgeUiState.Error -> {
                            Text(
                                text = (uiState as CreatePledgeUiState.Error).message,
                                color = Color.Red,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                        is CreatePledgeUiState.Idle -> {

                        }
                    }
            }},
            containerColor = Color(0xFF1A1B1E),
            shape = RoundedCornerShape(16.dp)
        )
            }


}



@Composable
fun DetailRow(label: String, value: String, padding: Dp = 4.dp) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = padding),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = Color.LightGray, fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

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

@Composable
fun CommentCard(comment: CommentResponseDto) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2F3035)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = comment.message,
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = "Posted on ${comment.createdAt}",
                color = Color.Gray,
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 4.dp)
            )

            comment.reply?.let { reply ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF3A3B3F)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = reply.message,
                            color = Color(0xFFD0D0D0),
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Reply on ${reply.createdAt}",
                            color = Color.Gray,
                            fontSize = 11.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownList(
    itemList: List<AccountDto>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    var selectedText = if (itemList.isNotEmpty()) {
        "${itemList[selectedIndex].name} - ${itemList[selectedIndex].balance} KWD"
    } else {
        "Select Account"
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,

            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White,
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF2A2B2E),
                unfocusedContainerColor = Color(0xFF2A2B2E)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFF2A2B2E))
        ) {
            itemList.forEachIndexed { index, account ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "${account.name} - ${account.balance} KWD",
                            color = Color.White
                        )
                    },
                    onClick = {
                        onItemClick(index)
                        selectedText = "${account.name} - ${account.balance} KWD"
                        expanded = false
                    }
                )
            }
        }
    }
}


