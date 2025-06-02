package com.coded.loanlift.screens.accounts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coded.loanlift.viewModels.DashboardViewModel
import com.coded.loanlift.viewModels.TransferUiState
import com.coded.loanlift.viewModels.AccountsUiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    viewModel: DashboardViewModel,
    sourceAccountNumber: String,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.transferUiState.collectAsState()
    val accountsUiState by viewModel.accountsUiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val allAccounts = when (accountsUiState) {
        is AccountsUiState.Success -> (accountsUiState as AccountsUiState.Success).accounts
        else -> emptyList()
    }

    val sourceAccount = allAccounts.find { it.accountNumber == sourceAccountNumber }
    val sourceName = sourceAccount?.name ?: "Unknown"

    var selectedAccountNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Reset form after success
    LaunchedEffect(uiState) {
        if (uiState is TransferUiState.Success) {
            selectedAccountNumber = ""
            amount = ""
            coroutineScope.launch {
                snackbarHostState.showSnackbar("✅ Transfer successful")
            }
            viewModel.resetTransferUiState()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        Column(modifier = Modifier.fillMaxSize()) {

            TopAppBar(
                title = { Text("Transfer Funds", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text("From: $sourceName", color = Color.White, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(24.dp))

                Text("To Account", color = Color.White)
                ExposedDropdownMenuBox(
                    expanded = isDropdownExpanded,
                    onExpandedChange = { isDropdownExpanded = !isDropdownExpanded }
                ) {
                    TextField(
                        value = allAccounts.find { it.accountNumber == selectedAccountNumber }?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Choose recipient") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        colors = ExposedDropdownMenuDefaults.textFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        allAccounts
                            .filter { it.accountNumber != sourceAccountNumber }
                            .forEach { account ->
                                DropdownMenuItem(
                                    text = { Text(account.name) },
                                    onClick = {
                                        selectedAccountNumber = account.accountNumber
                                        isDropdownExpanded = false
                                    }
                                )
                            }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (KWD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(
                        onClick = { onBackClick() },
                        modifier = Modifier.weight(1f),
                        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text("Cancel")
                    }

                    val parsedAmount = amount.toBigDecimalOrNull()
                    val canTransfer = selectedAccountNumber.isNotBlank() && parsedAmount != null

                    Button(
                        onClick = {
                            viewModel.transferBetweenAccounts(sourceAccountNumber, selectedAccountNumber, parsedAmount!!)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0)),
                        enabled = uiState !is TransferUiState.Loading && canTransfer
                    ) {
                        Text("Transfer", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState is TransferUiState.Loading) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            SnackbarHost(hostState = snackbarHostState)
        }
    }
}