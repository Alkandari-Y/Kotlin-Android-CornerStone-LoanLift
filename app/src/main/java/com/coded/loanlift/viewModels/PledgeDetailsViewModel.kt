package com.coded.loanlift.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.enums.CampaignStatus
import com.coded.loanlift.data.response.campaigns.CampaignPublicDetails
import com.coded.loanlift.data.response.pledges.PledgeTransactionWithDetails
import com.coded.loanlift.data.response.pledges.UpdatePledgeRequest
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.providers.RetrofitInstance
import com.coded.loanlift.repositories.PledgeRepository
import com.coded.loanlift.repositories.PledgeTransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

sealed class PledgeTransactionsUiState {
    data object Loading : PledgeTransactionsUiState()
    data class Success(val pledgeTransactions: List<PledgeTransactionWithDetails>) : PledgeTransactionsUiState()
    data class Error(val message: String) : PledgeTransactionsUiState()
}

sealed class PledgeDetailsUiState{
    data object Loading: PledgeDetailsUiState()
    data class Success(val pledgeDetails: UserPledgeDto): PledgeDetailsUiState()
    data class Error(val message: String) : PledgeDetailsUiState()
}

sealed class CampaignDetailsUiState {
    data object Loading : CampaignDetailsUiState()
    data class Success(val campaignDetails: CampaignPublicDetails) : CampaignDetailsUiState()
    data class Error(val message: String) : CampaignDetailsUiState()
}

class PledgeDetailsViewModel(
    private val context: Context
): ViewModel() {

    private val _pledgeDetailsUiState= MutableStateFlow<PledgeDetailsUiState>(PledgeDetailsUiState.Loading)
    val pledgeDetailsUiState: StateFlow<PledgeDetailsUiState> = _pledgeDetailsUiState

    private val _pledgeTransactionsUiState = MutableStateFlow<PledgeTransactionsUiState>(PledgeTransactionsUiState.Loading)
    val pledgesUiState: StateFlow<PledgeTransactionsUiState> = _pledgeTransactionsUiState

    private val _campaignDetailsUiState = MutableStateFlow<CampaignDetailsUiState>(CampaignDetailsUiState.Loading)
    val campaignDetailsUiState: StateFlow<CampaignDetailsUiState> = _campaignDetailsUiState

    private val _shouldNavigate = MutableStateFlow(false)
    val shouldNavigate: StateFlow<Boolean> = _shouldNavigate

    fun resetNavigationFlag() {
        _shouldNavigate.value = false
    }

    fun loadPledgeDetails(pledgeId: Long) {
        _pledgeDetailsUiState.value = PledgeDetailsUiState.Loading

        val pledgeDetailsExist = PledgeRepository.getCachedPledgeById(pledgeId)

        if (pledgeDetailsExist != null) {
            _pledgeDetailsUiState.value = PledgeDetailsUiState.Success(pledgeDetailsExist)
            loadCampaignDetails(pledgeDetailsExist.campaignId)
        } else {
            viewModelScope.launch {
                try {
                    val response = RetrofitInstance.getCampaignApiService(context).getPledgeDetails(pledgeId)
                    if (response.isSuccessful) {
                        val pledge = response.body()!!
                        _pledgeDetailsUiState.value = PledgeDetailsUiState.Success(pledge)
                        loadCampaignDetails(pledge.campaignId)
                    } else {
                        _pledgeDetailsUiState.value = PledgeDetailsUiState.Error("Error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _pledgeDetailsUiState.value = PledgeDetailsUiState.Error(e.message ?: "Unexpected error")
                }
            }
        }
    }

    fun loadPledgeTransactions(pledgeId: Long) {
        _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Loading

        val transactionsExist = PledgeTransactionRepository.findTransactionsByPledgeId(pledgeId)

        if (transactionsExist != null) {
            _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Success(transactionsExist)
        } else {
            viewModelScope.launch {
                try {
                    val response = RetrofitInstance.getCampaignApiService(context).getPledgeDetailedTransactions(pledgeId)
                    if (response.isSuccessful) {
                        _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Success(response.body() ?: listOf())
                    } else {
                        _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Error("Error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Error(e.message ?: "Unexpected error")
                }
            }
        }
    }

    fun loadCampaignDetails(campaignId: Long) {
        _campaignDetailsUiState.value = CampaignDetailsUiState.Loading

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.getCampaignApiService(context).getCampaignDetails(campaignId)
                if (response.isSuccessful) {
                    _campaignDetailsUiState.value = CampaignDetailsUiState.Success(response.body()!!)
                } else {
                    _campaignDetailsUiState.value = CampaignDetailsUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _campaignDetailsUiState.value = CampaignDetailsUiState.Error(e.message ?: "Failed to load campaign")
            }
        }
    }

    fun withdrawPledge(pledgeId: Long) {
        val campaign = (_campaignDetailsUiState.value as? CampaignDetailsUiState.Success)?.campaignDetails
        if (campaign?.status == CampaignStatus.ACTIVE) {
            viewModelScope.launch {
                try {
                    val response = RetrofitInstance.getCampaignApiService(context).withdrawPledge(pledgeId)
                    if (response.isSuccessful) {
                        loadPledgeDetails(pledgeId)
                        loadPledgeTransactions(pledgeId)
                    }
                } catch (_: Exception) {
                }
            }
        }
    }

    fun updatePledge(pledgeId: Long, newAmount: BigDecimal) {
        val campaign = (_campaignDetailsUiState.value as? CampaignDetailsUiState.Success)?.campaignDetails
        if (campaign?.status == CampaignStatus.ACTIVE) {
            viewModelScope.launch {
                try {
                    val response = RetrofitInstance.getCampaignApiService(context).updatePledge(
                        pledgeId,
                        UpdatePledgeRequest(amount = newAmount)
                    )
                    if (response.isSuccessful) {
                        loadPledgeDetails(pledgeId)
                        loadPledgeTransactions(pledgeId)
                    }
                } catch (_: Exception) {
                }
            }
        }
    }
}
