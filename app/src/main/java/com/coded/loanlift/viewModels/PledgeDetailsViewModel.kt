package com.coded.loanlift.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.response.pledges.PledgeTransactionWithDetails
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.providers.RetrofitInstance
import com.coded.loanlift.repositories.PledgeRepository
import com.coded.loanlift.repositories.PledgeTransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



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


class PledgeDetailsViewModel(
    private val context: Context): ViewModel()
{

    private val _pledgeDetailsUiState= MutableStateFlow<PledgeDetailsUiState>(PledgeDetailsUiState.Loading)
    val pledgeDetailsUiState: MutableStateFlow<PledgeDetailsUiState> = _pledgeDetailsUiState

    private val _pledgeTransactionsUiState = MutableStateFlow<PledgeTransactionsUiState>(PledgeTransactionsUiState.Loading)
    val pledgesUiState: MutableStateFlow<PledgeTransactionsUiState> = _pledgeTransactionsUiState


    private val _shouldNavigate = MutableStateFlow(false)
    val shouldNavigate: StateFlow<Boolean> = _shouldNavigate

    fun resetNavigationFlag() {
        _shouldNavigate.value = false
    }

    fun loadPledgeDetails(pledgeId: Long) {
        _pledgeDetailsUiState.value = PledgeDetailsUiState.Loading

        val pledgeDetailsExist = PledgeRepository // or whatever repository you use for pledge details
            .getCachedPledgeById(pledgeId)

        if (pledgeDetailsExist != null) {
            _pledgeDetailsUiState.value = PledgeDetailsUiState.Success(pledgeDetailsExist)
            return
        } else {
            viewModelScope.launch {
                try {
                    val response = RetrofitInstance
                        .getCampaignApiService(context)
                        .getPledgeDetails(pledgeId)
                    if (response.isSuccessful) {
                        _pledgeDetailsUiState.value = PledgeDetailsUiState
                            .Success(pledgeDetails = response.body()!!)
                    } else {
                        _pledgeDetailsUiState.value = PledgeDetailsUiState.Error("Error: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _pledgeDetailsUiState.value = PledgeDetailsUiState.Error(e.message ?: "Wrong")
                }
            }
        }
    }


    fun loadPledgeTransactions(pledgeId: Long){
        _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Loading

        val transactionsExist = PledgeTransactionRepository
            .findTransactionsByPledgeId(pledgeId)

        if (transactionsExist != null) {
            _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Success(transactionsExist)
            return
        } else {
            viewModelScope.launch {
                try {
                    val response = RetrofitInstance
                        .getCampaignApiService(context)
                        .getPledgeDetailedTransactions(pledgeId)
                    if (response.isSuccessful) {
                        _pledgeTransactionsUiState.value = PledgeTransactionsUiState
                            .Success(pledgeTransactions = response.body()
                                ?: listOf())
                    } else {
                        _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Error("Error: ${response.code()}")
                    }
                } catch (e: Exception){
                    _pledgeTransactionsUiState.value = PledgeTransactionsUiState.Error(e.message?: "Wrong")
                }
            }
        }
    }
}