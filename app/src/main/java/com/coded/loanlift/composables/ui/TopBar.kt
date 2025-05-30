package com.coded.loanlift.composables.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.coded.loanlift.providers.RetrofitInstance
import com.coded.loanlift.repositories.UserRepository
import java.time.LocalTime


@Composable
fun TopBar(
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val context = LocalContext.current
    val greeting = remember { getGreetingForCurrentTime() }
    var expanded by remember { mutableStateOf(false) }
    var fullName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (UserRepository.kyc == null) {
            try {
                val response = RetrofitInstance.getBankingServiceProvide(context).getUserKyc()
                if (response.isSuccessful) {
                    val kycData = response.body()
                    UserRepository.kyc = kycData
                    fullName = "${kycData?.firstName} ${kycData?.lastName}"
                    Log.d("TopBar", "Loaded KYC: $kycData")
                } else {
                    Log.w("TopBar", "Failed to load KYC: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("TopBar", "KYC error: ${e.message}")
            }
        } else {
            val kyc = UserRepository.kyc
            fullName = "${kyc?.firstName} ${kyc?.lastName}"
        }
    }

    Row(
        modifier = Modifier
            .padding(top = 10.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Column {
                Text(
                    text = "$greeting,",
                    color = Color.White,
                    fontSize = 14.sp
                )
                fullName?.let {
                    Text(
                        text = it,
                        color = Color(0xFF4B8BFF),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
            }
            IconButton(onClick = { /* TODO */ }) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Gray)
                    .clickable { expanded = true },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Profile Menu",
                    tint = Color.White,
                    modifier = Modifier.fillMaxSize()
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Profile") },
                        onClick = {
                            expanded = false
                            onProfileClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Logout") },
                        onClick = {
                            expanded = false
                            onLogoutClick()
                        }
                    )
                }
            }
        }
    }
}

private fun getGreetingForCurrentTime(): String {
    val hour = LocalTime.now().hour
    return when (hour) {
        in 5..11 -> "Good Morning"
        in 12..16 -> "Good Afternoon"
        in 17..20 -> "Good Evening"
        else -> "Good Night"
    }
}