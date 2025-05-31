package com.coded.loanlift.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.data.response.campaigns.CampaignListItemResponse
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.providers.RetrofitInstance
import com.coded.loanlift.repositories.CategoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AccountsUiState {
    data object Loading : AccountsUiState()
    data class Success(val accounts: List<AccountDto>) : AccountsUiState()
    data class Error(val message: String) : AccountsUiState()
}

sealed class CampaignsUiState {
    data object Loading : CampaignsUiState()
    data class Success(val campaigns: List<CampaignListItemResponse>) : CampaignsUiState()
    data class Error(val message: String) : CampaignsUiState()
}

sealed class PledgesUiState {
    data object Loading : PledgesUiState()
    data class Success(val pledges: List<UserPledgeDto>) : PledgesUiState()
    data class Error(val message: String) : PledgesUiState()
}

class DashboardViewModel(
    private val context: Context
): ViewModel() {

    private val _accountsUiState = MutableStateFlow<AccountsUiState>(AccountsUiState.Loading)
    val accountsUiState: StateFlow<AccountsUiState> = _accountsUiState

    private val _campaignsUiState = MutableStateFlow<CampaignsUiState>(CampaignsUiState.Loading)
    val campaignsUiState: StateFlow<CampaignsUiState> = _campaignsUiState

    private val _pledgesUiState = MutableStateFlow<PledgesUiState>(PledgesUiState.Loading)
    val pledgesUiState: StateFlow<PledgesUiState> = _pledgesUiState


    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.getBankingServiceProvide(context).getAllCategories()
                if (response.isSuccessful) {
                    val categories = response.body().orEmpty()
                    CategoryRepository.categories = categories
                } else {
                    Log.e("DashboardViewModel", "Categories fetch failed: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("DashboardViewModel", "Error fetching categories: ${e.message}")
            }
        }
    }

    fun fetchAccounts() {
        viewModelScope.launch {
            delay(500)
            _accountsUiState.value = AccountsUiState.Loading
            try {
                val response = RetrofitInstance.getBankingServiceProvide(context).getAllAccounts()
                if (response.isSuccessful) {
                    _accountsUiState.value = AccountsUiState.Success(response.body().orEmpty())
                } else {
                    _accountsUiState.value = AccountsUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _accountsUiState.value = AccountsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchCampaigns() {
        viewModelScope.launch {
            delay(500)
            _campaignsUiState.value = CampaignsUiState.Loading
            try {
                val response = RetrofitInstance.getCampaignApiService(context).getMyCampaigns()
                if (response.isSuccessful) {
                    _campaignsUiState.value = CampaignsUiState.Success(response.body().orEmpty())
                } else {
                    _campaignsUiState.value = CampaignsUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _campaignsUiState.value = CampaignsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun fetchPledges() {
        viewModelScope.launch {
            delay(500)
            _pledgesUiState.value = PledgesUiState.Loading
            try {
                val response = RetrofitInstance.getCampaignApiService(context).getAllMyPledges()
                if (response.isSuccessful) {
                    _pledgesUiState.value = PledgesUiState.Success(response.body().orEmpty())
                } else {
                    _pledgesUiState.value = PledgesUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _pledgesUiState.value = PledgesUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
