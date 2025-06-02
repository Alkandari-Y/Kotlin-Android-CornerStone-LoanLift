package com.coded.loanlift.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.response.pledges.PledgeTransactionWithDetails
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.providers.RetrofitInstance
import com.coded.loanlift.repositories.AccountRepository
import com.coded.loanlift.repositories.PledgeRepository
import com.coded.loanlift.repositories.PledgeTransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



sealed class PledgeTransationsUiState {
    data object Loading : PledgeTransationsUiState()
    data class Success(val pledgeTransactions: List<PledgeTransactionWithDetails>) : PledgeTransationsUiState()
    data class Error(val message: String) : PledgeTransationsUiState()
}

sealed class PledgeDetailsUiState{
    data object Loading: PledgeDetailsUiState()
    data class Success(val pledgeDetails: UserPledgeDto): PledgeDetailsUiState()
    data class Error(val message: String) : PledgeDetailsUiState()

}


class PledgeDetailsViewModel(
    private val context: Context): ViewModel()
{

    private val _pledgeDetailsUiState= MutableStateFlow<PledgeDetailsUiState?>(PledgeDetailsUiState.Loading)
    val pledgeDetailsUiState: MutableStateFlow<PledgeDetailsUiState?> = _pledgeDetailsUiState

    private val _pledgeTransationsUiState = MutableStateFlow<PledgeTransationsUiState?>(PledgeTransationsUiState.Loading)
    val pledgesUiState: MutableStateFlow<PledgeTransationsUiState?> = _pledgeTransationsUiState


    private val _shouldNavigate = MutableStateFlow(false)
    val shouldNavigate: StateFlow<Boolean> = _shouldNavigate

    fun resetNavigationFlag() {
        _shouldNavigate.value = false
    }

    fun loadPledgeTransactions(pledgeId: Long){
        _pledgeTransationsUiState.value = PledgeTransationsUiState.Loading

        val transactionsExist = PledgeTransactionRepository
            .findTransactionsByPledgeId(pledgeId)

        if (transactionsExist != null) {
            _pledgeTransationsUiState.value = PledgeTransationsUiState.Success(transactionsExist)
            return
        } else {
            viewModelScope.launch {
                try {
                    val response = RetrofitInstance
                        .getCampaignApiService(context)
                        .getPledgeDetailedTransactions(pledgeId)
                    if (response.isSuccessful) {
                        _pledgeTransationsUiState.value = PledgeTransationsUiState
                            .Success(pledgeTransactions = response.body()
                                ?: listOf())
                    } else {
                        _pledgeTransationsUiState.value = PledgeTransationsUiState.Error("Error: ${response.code()}")
                    }
                } catch (e: Exception){
                    _pledgeTransationsUiState.value = PledgeTransationsUiState.Error(e.message?: "Wrong")
                }
            }
        }
    }
}