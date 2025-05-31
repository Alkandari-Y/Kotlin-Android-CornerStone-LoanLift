package com.coded.loanlift.viewModels


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.response.accounts.AccountCreateRequest
import com.coded.loanlift.data.response.accounts.AccountDto
import com.coded.loanlift.formStates.auth.AccountCreateForm
import com.coded.loanlift.repositories.AccountRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class AccountViewModel(
    private val context: Context
) : ViewModel() {

    sealed class AccountCreateUiState {
        data object Idle : AccountCreateUiState()
        data object Loading : AccountCreateUiState()
        data class Success(val account: AccountDto) : AccountCreateUiState()
        data class Error(val message: String) : AccountCreateUiState()
    }

    private val _formState = MutableStateFlow(AccountCreateForm())
    val formState: StateFlow<AccountCreateForm> = _formState

    private val _accountUiState = MutableStateFlow<AccountCreateUiState>(AccountCreateUiState.Idle)
    val accountUiState: StateFlow<AccountCreateUiState> = _accountUiState

    private val repository = AccountRepository(context)

    fun updateName(name: String) {
        _formState.value = _formState.value.copy(name = name, nameError = null)
    }

    fun updateInitialBalance(balance: String) {
        _formState.value = _formState.value.copy(initialBalance = balance, balanceError = null)
    }

    fun submitAccount() {
        val form = _formState.value
        var isValid = true

        if (form.name.isBlank()) {
            _formState.value = form.copy(nameError = "Account name cannot be blank")
            isValid = false
        }

        val balance = form.initialBalance.toBigDecimalOrNull()
        if (balance == null || balance < BigDecimal("100.00")) {
            _formState.value = form.copy(balanceError = "Initial balance must be at least 100.00")
            isValid = false
        }

        if (!isValid) return

        _accountUiState.value = AccountCreateUiState.Loading

        viewModelScope.launch {
            try {
                val request = AccountCreateRequest(
                    name = form.name,
                    initialBalance = balance!!
                )

                val result = repository.createAccount(request)

                result.onSuccess {
                    _accountUiState.value = AccountCreateUiState.Success(it)
                }.onFailure {
                    _accountUiState.value = AccountCreateUiState.Error(it.message ?: "Unknown error")
                }

            } catch (e: Exception) {
                _accountUiState.value = AccountCreateUiState.Error(e.message ?: "Something went wrong")
            }
        }
    }
}
