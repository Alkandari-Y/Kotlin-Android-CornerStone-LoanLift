package com.coded.loanlift.viewModels


import android.content.Context
import com.coded.loanlift.data.response.kyc.KYCRequest
import com.coded.loanlift.data.response.kyc.KYCResponse
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.formStates.KycFormState
import com.coded.loanlift.managers.TokenManager
import com.coded.loanlift.providers.RetrofitInstance
import com.coded.loanlift.repositories.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import java.math.BigDecimal


sealed class UiStatus {
    object Idle : UiStatus()
    object Loading : UiStatus()
    object Success : UiStatus()
    data class Error(val message: String) : UiStatus()
}

class KycViewModel(private val context: Context): ViewModel() {

    var formState = mutableStateOf(KycFormState())
    var isEditMode = mutableStateOf(false)

    private val _status = MutableStateFlow<UiStatus>(UiStatus.Idle)
    val status: StateFlow<UiStatus> = _status

    private val bankingService = RetrofitInstance.getBankingServiceProvide(context)

    init {
        initializeKycIfAvailable()
    }

    private fun initializeKycIfAvailable() {
        val tokenInfo = TokenManager.decodeAccessToken(context)
        if (tokenInfo?.isActive == true) {
            viewModelScope.launch {
                _status.value = UiStatus.Loading
                try {
                    Log.d("KYC_DEBUG", "Calling getUserKyc()")
                    val response = bankingService.getUserKyc()
                    Log.d("KYC_DEBUG", "KYC Response: ${response.body()}")  ;
                    if (response.isSuccessful) {
                        response.body()?.let { kyc ->
                            loadKyc(kyc)
                            isEditMode.value = false
                            _status.value = UiStatus.Idle
                        } ?: run {
                            _status.value = UiStatus.Error("No KYC data found")
                        }
                    } else {
                        _status.value = UiStatus.Error("Failed to load KYC: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _status.value = UiStatus.Error("Error fetching KYC: ${e.message}")
                }
            }
        }
        else {
            isEditMode.value = true
        }
    }

    fun loadKyc(data: KYCResponse) {
        formState.value = KycFormState(
            firstName = data.firstName,
            lastName = data.lastName,
            dateOfBirth = data.dateOfBirth.orEmpty(),
            salary = data.salary.toPlainString(),
            nationality = data.nationality
        )
    }

    fun submitKyc() {
        formState.value = formState.value.validate()
        if (!formState.value.formIsValid) return

        viewModelScope.launch {
            _status.value = UiStatus.Loading
            try {
                val request = KYCRequest(
                    firstName = formState.value.firstName,
                    lastName = formState.value.lastName,
                    dateOfBirth = formState.value.dateOfBirth,
                    salary = formState.value.salary.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    nationality = formState.value.nationality
                )
                val updatedKyc: KYCResponse =
                    RetrofitInstance.getBankingServiceProvide(context).updateKyc(request)

                UserRepository.kyc = updatedKyc
                TokenManager.refreshToken(context)

                loadKyc(updatedKyc)
                initializeKycIfAvailable()

                _status.value = UiStatus.Success
                isEditMode.value = false
            } catch (e: Exception) {
                _status.value = UiStatus.Error("KYC update failed: ${e.message}")
            }
        }
    }
}