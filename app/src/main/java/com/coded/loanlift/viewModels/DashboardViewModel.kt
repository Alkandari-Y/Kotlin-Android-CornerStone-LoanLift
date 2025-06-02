package com.coded.loanlift.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.mappers.toCampaignListItemResponse
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.data.response.campaigns.CampaignDetailResponse
import com.coded.loanlift.data.response.campaigns.CampaignListItemResponse
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails
import com.coded.loanlift.data.response.campaigns.CampaignTransactionHistoryResponse
import com.coded.loanlift.data.response.comments.CommentCreateRequest
import com.coded.loanlift.data.response.comments.CommentResponseDto
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.formStates.campaigns.CampaignFormState
import com.coded.loanlift.providers.RetrofitInstance
import com.coded.loanlift.repositories.AccountRepository
import com.coded.loanlift.repositories.CategoryRepository
import com.coded.loanlift.repositories.UserRepository
import com.coded.loanlift.utils.prepareCampaignFormParts
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

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

sealed class PublicCampaignsUiState {
    data object Loading : PublicCampaignsUiState()
    data class Success(val campaigns: List<CampaignListItemResponse>) : PublicCampaignsUiState()
    data class Error(val message: String) : PublicCampaignsUiState()
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


sealed class CommentsUiState {
    data object Loading: CommentsUiState()
    data class Success(val comments: List<CommentResponseDto>): CommentsUiState()
    data class Error(val message: String): CommentsUiState()
}

sealed class PostCommentUiState {
    data object Idle: PostCommentUiState()
    data object Loading: PostCommentUiState()
    data object Success: PostCommentUiState()
    data class Error(val message: String): PostCommentUiState()
}

sealed class CreateCampaignUiState {
    data object Idle: CreateCampaignUiState()
    data object Loading: CreateCampaignUiState()
    data object Success: CreateCampaignUiState()
    data class Error(val message: String): CreateCampaignUiState()
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

    private val _publicCampaignsUiState = MutableStateFlow<PublicCampaignsUiState>(PublicCampaignsUiState.Loading)
    val publicCampaignsUiState: StateFlow<PublicCampaignsUiState> = _publicCampaignsUiState

    private val _commentsUiState = MutableStateFlow<CommentsUiState>(CommentsUiState.Loading)
    val commentsUiState: StateFlow<CommentsUiState> = _commentsUiState

    private val _postCommentsUiState = MutableStateFlow<PostCommentUiState>(PostCommentUiState.Idle)
    val postCommentsUiState: StateFlow<PostCommentUiState> = _postCommentsUiState


    private val _createCampaignUiState = MutableStateFlow<CreateCampaignUiState>(CreateCampaignUiState.Idle)
    val createCampaignUiState: StateFlow<CreateCampaignUiState> = _createCampaignUiState


    init {
        viewModelScope.launch {
            if (UserRepository.userInfo == null) {
                UserRepository.loadUserInfo(context)
            }
            fetchPublicCampaigns()
            fetchCampaigns()
            fetchPledges()
            fetchAccounts()
            fetchCategories()
        }
    }

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

    fun fetchPublicCampaigns() {
        viewModelScope.launch {
            delay(500)
            _publicCampaignsUiState.value = PublicCampaignsUiState.Loading
            try {
                val response = RetrofitInstance.getCampaignApiService(context).getAllCampaigns()
                if (response.isSuccessful) {
                    _publicCampaignsUiState.value = PublicCampaignsUiState.Success(response.body().orEmpty())
                } else {
                    _publicCampaignsUiState.value = PublicCampaignsUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _publicCampaignsUiState.value = PublicCampaignsUiState.Error(e.message ?: "Unknown error")
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


    fun fetchCampaignComments(campaignId: Long) {
        viewModelScope.launch {
            _commentsUiState.value = CommentsUiState.Loading
            try {
                val response = RetrofitInstance
                    .getCampaignApiService(context)
                    .allAllCampaignComments(campaignId)

                if (response.isSuccessful) {
                    val comments = response.body()
                    _commentsUiState.value = CommentsUiState.Success(comments = comments!!)
                } else {
                    _commentsUiState.value = CommentsUiState.Error("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _commentsUiState.value = CommentsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun createCampaign(form: CampaignFormState, context: Context) {
        val validatedForm = form.validate()
        if (!validatedForm.isValid) {
            // update state with errors
            return
        }

        viewModelScope.launch {
            _createCampaignUiState.value = CreateCampaignUiState.Loading
            try {
                val (fields, imagePart) = prepareCampaignFormParts(validatedForm, context)

                val response = RetrofitInstance.getCampaignApiService(context)
                    .createCampaign(fields, imagePart)

                if (response.isSuccessful) {
                    val newCampaign = response.body()?.toCampaignListItemResponse()
                    val currentCampaignsOwned = (_campaignsUiState.value as? CampaignsUiState.Success)?.campaigns ?: emptyList()
                    val updatedCampaigns = currentCampaignsOwned + newCampaign
                    _campaignsUiState.value = CampaignsUiState.Success(campaigns = updatedCampaigns.filterNotNull())
                } else {
                    _createCampaignUiState.value = CreateCampaignUiState.Error(response.message())
                }
            } catch (e: Exception) {
                _createCampaignUiState.value = CreateCampaignUiState.Error("Error: ${e.message}")

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

    fun postComment(campaignId: Long, message: String) {
        viewModelScope.launch {
            _postCommentsUiState.value = PostCommentUiState.Loading
            try {
                val response = RetrofitInstance.getCampaignApiService(context).addComment(
                    campaignId = campaignId,
                    CommentCreateRequest(message = message)
                )

                if (response.isSuccessful) {
                    val newComment = response.body()
                    if (newComment != null) {
                        val currentComments = (_commentsUiState.value as? CommentsUiState.Success)?.comments ?: emptyList()
                        val updatedComments = (currentComments + newComment)

                        _commentsUiState.value = CommentsUiState.Success(comments = updatedComments)
                    }
                    _postCommentsUiState.value = PostCommentUiState.Success
                } else {
                    _postCommentsUiState.value = PostCommentUiState.Error("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _postCommentsUiState.value = PostCommentUiState.Error("Error: ${e.message}")
            }
        }
    }

}
