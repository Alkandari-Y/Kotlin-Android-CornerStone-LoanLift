package com.coded.loanlift.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.data.response.campaigns.CampaignListItemResponse
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails
import com.coded.loanlift.data.response.campaigns.CampaignTransactionHistoryResponse
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.providers.RetrofitInstance
import com.coded.loanlift.repositories.AccountRepository
import com.coded.loanlift.repositories.CategoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

sealed class DeleteCampaignUiState {
    data object Idle: DeleteCampaignUiState()
    data object Loading: DeleteCampaignUiState()
    data object Success: DeleteCampaignUiState()
    data class Error(val message: String): DeleteCampaignUiState()
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

    private val _campaignDetailUiState = MutableStateFlow<CampaignDetailUiState>(CampaignDetailUiState.Loading)
    val campaignDetailUiState: StateFlow<CampaignDetailUiState> = _campaignDetailUiState

    private val _campaignHistoryUiState = MutableStateFlow<CampaignHistoryUiState>(CampaignHistoryUiState.Loading)
    val campaignHistoryUiState: StateFlow<CampaignHistoryUiState> = _campaignHistoryUiState

    private val _deleteCampaignUiState = MutableStateFlow<DeleteCampaignUiState>(DeleteCampaignUiState.Idle)
    val deleteCampaignUiState: StateFlow<DeleteCampaignUiState> = _deleteCampaignUiState

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
                    AccountRepository.myAccounts = response.body()?.toMutableList() ?: mutableListOf()
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

                    if (AccountRepository.myAccounts.isEmpty()) {
                        val accountResponse = RetrofitInstance.getBankingServiceProvide(context).getAllAccounts()
                        if (accountResponse.isSuccessful) {
                            AccountRepository.myAccounts = accountResponse.body()?.toMutableList() ?: mutableListOf()
                        }
                    }

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

    fun deleteCampaign(campaignId: Long) {
        viewModelScope.launch {
            _deleteCampaignUiState.value = DeleteCampaignUiState.Loading
            try {
                val response = RetrofitInstance.getCampaignApiService(context).deleteCampaign(campaignId)
                if (response.isSuccessful) {
                    _deleteCampaignUiState.value = DeleteCampaignUiState.Success
                    fetchCampaigns()
                } else {
                    _deleteCampaignUiState.value = DeleteCampaignUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _deleteCampaignUiState.value = DeleteCampaignUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}
