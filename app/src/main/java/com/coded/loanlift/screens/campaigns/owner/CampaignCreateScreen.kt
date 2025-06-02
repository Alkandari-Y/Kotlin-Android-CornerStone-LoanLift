package com.coded.loanlift.screens.campaigns.owner

import android.app.DatePickerDialog
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.coded.loanlift.composables.campaigns.CampaignExploreCard
import com.coded.loanlift.composables.campaigns.SkeletonCampaignCard
import com.coded.loanlift.composables.dashboard.SectionLoading
import com.coded.loanlift.formStates.campaigns.CampaignFormState
import com.coded.loanlift.repositories.CategoryRepository
import com.coded.loanlift.utils.FileUtils
import com.coded.loanlift.viewModels.DashboardViewModel
import com.coded.loanlift.viewModels.PublicCampaignsUiState
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignCreateScreen(
    viewModel: DashboardViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val categories = CategoryRepository.categories
    var formState by remember { mutableStateOf(CampaignFormState()) }
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val calendar = remember { Calendar.getInstance() }

    val imageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val mimeType = context.contentResolver.getType(uri)
            val allowedTypes = listOf("image/jpeg", "image/png", "image/webp")

            if (mimeType !in allowedTypes) {
                formState = formState.copy(imageFile = null, imageError = "Only JPEG, PNG, or WEBP images are allowed.")
                return@let
            }

            val file = FileUtils.getFileFromUri(context, uri)
            formState = formState.copy(imageFile = file, imageError = null)
        }
    }


    if (showDatePicker) {
        LaunchedEffect(Unit) {
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    val dateStr = "%02d-%02d-%04d".format(dayOfMonth, month + 1, year)
                    formState = formState.copy(campaignDeadline = dateStr)
                    showDatePicker = false
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    Scaffold(
        containerColor = Color(0xFF1A1B1E),
        topBar = {
            TopAppBar(
                title = {
                    Text("Create A Campaign", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2A2B2E))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = formState.title,
                onValueChange = { formState = formState.copy(title = it) },
                label = { Text("Title", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                isError = formState.titleError != null,
                modifier = Modifier.fillMaxWidth()
            )
            formState.titleError?.let { Text(it, color = Color.Red) }

            OutlinedTextField(
                value = formState.description,
                onValueChange = { formState = formState.copy(description = it) },
                label = { Text("Description", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                isError = formState.descriptionError != null,
                modifier = Modifier.fillMaxWidth()
            )
            formState.descriptionError?.let { Text(it, color = Color.Red) }

            OutlinedTextField(
                value = formState.goalAmount,
                onValueChange = { formState = formState.copy(goalAmount = it) },
                label = { Text("Goal Amount", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                isError = formState.goalAmountError != null,
                modifier = Modifier.fillMaxWidth()
            )
            formState.goalAmountError?.let { Text(it, color = Color.Red) }

            OutlinedTextField(
                value = formState.campaignDeadline,
                onValueChange = {},
                label = { Text("Campaign Deadline", color = Color.White) },
                textStyle = LocalTextStyle.current.copy(color = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                readOnly = true,
                isError = formState.campaignDeadlineError != null
            )
            formState.campaignDeadlineError?.let { Text(it, color = Color.Red) }

            Box {
                OutlinedTextField(
                    value = categories.find { it.id == formState.categoryId }?.name ?: "Select Category",
                    onValueChange = {},
                    label = { Text("Category", color = Color.White) },
                    textStyle = LocalTextStyle.current.copy(color = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    readOnly = true,
                    isError = formState.categoryIdError != null
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(category.name) },
                            onClick = {
                                formState = formState.copy(categoryId = category.id)
                                expanded = false
                            }
                        )
                    }
                }
            }
            formState.categoryIdError?.let { Text(it, color = Color.Red) }

            if (formState.imageFile != null) {
                Column {
                    Text("Selected Image: ${formState.imageFile!!.name}", color = Color.White)
                    Button(
                        onClick = { formState = formState.copy(imageFile = null) },
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text("Remove Image")
                    }
                }
            } else {
                Button(onClick = { imageLauncher.launch("image/*") }) {
                    Text("Choose Image")
                }
            }
            formState.imageError?.let { Text(it, color = Color.Red) }

            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = rememberAsyncImagePainter(formState.imageFile),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Button(
                onClick = {
                    val validated = formState.validate()
                    if (validated.isValid) {
                        viewModel.createCampaign(validated, context)
                        onBackClick()
                    } else {
                        formState = validated
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit Campaign")
            }
        }
    }
}
