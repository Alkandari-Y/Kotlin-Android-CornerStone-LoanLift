package com.coded.loanlift.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.response.accounts.AccountCreateRequest
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails
import com.coded.loanlift.data.response.campaigns.CampaignTransactionHistoryResponse
import com.coded.loanlift.providers.RetrofitInstance
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class CampaignDetailUiState {
    data object Loading : CampaignDetailUiState()
    data class Success(val campaign: CampaignOwnerDetails) : CampaignDetailUiState()
    data class Error(val message: String) : CampaignDetailUiState()
}

sealed class CampaignHistoryUiState {
    data object Loading: CampaignHistoryUiState()
    data class Success(
        val campaignTransactionHistory: CampaignTransactionHistoryResponse
    ): CampaignHistoryUiState()
    data class Error(val message: String): CampaignHistoryUiState()
}

class CampaignOwnerViewModel(
    private val context: Context
): ViewModel() {
    private val _campaignDetailUiState = MutableStateFlow<CampaignDetailUiState>(CampaignDetailUiState.Loading)
    val campaignDetailUiState: StateFlow<CampaignDetailUiState> = _campaignDetailUiState

    private val _campaignHistoryUiState = MutableStateFlow<CampaignHistoryUiState>(CampaignHistoryUiState.Loading)
    val campaignHistoryUiState: StateFlow<CampaignHistoryUiState> = _campaignHistoryUiState

    fun fetchCampaignDetail(campaignId: Long) {
        viewModelScope.launch {
            _campaignDetailUiState.value = CampaignDetailUiState.Loading
            try {
                val response = RetrofitInstance
                    .getCampaignApiService(context)
                    .getMyCampaignById(campaignId)

                if (response.isSuccessful) {
                    val campaign = response.body()
                    _campaignDetailUiState.value = CampaignDetailUiState.Success(campaign!!)
                } else {
                    _campaignDetailUiState.value = CampaignDetailUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _campaignDetailUiState.value = CampaignDetailUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchCampaignTransactionHistory(campaignId: Long) {
        viewModelScope.launch {
            _campaignHistoryUiState.value = CampaignHistoryUiState.Loading
            try {
                val response = RetrofitInstance
                    .getCampaignApiService(context)
                    .getMyCampaignTransactionsById(campaignId)

                if (response.isSuccessful) {
                    val campaignTransactionHistory = response.body()
                    _campaignHistoryUiState.value = CampaignHistoryUiState.Success(campaignTransactionHistory!!)
                } else {
                    _campaignHistoryUiState.value = CampaignHistoryUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _campaignHistoryUiState.value = CampaignHistoryUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}