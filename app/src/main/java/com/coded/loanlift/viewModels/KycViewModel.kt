package com.coded.loanlift.viewModels


import android.content.Context
import com.coded.loanlift.data.response.kyc.KYCRequest
import com.coded.loanlift.data.response.kyc.KYCResponse
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.providers.RetrofitInstance
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

import java.math.BigDecimal

class KycViewModel(private val context: Context): ViewModel() {

    var firstName = mutableStateOf("")
    var lastName = mutableStateOf("")
    var dateOfBirth = mutableStateOf("")
    var salary = mutableStateOf("")
    var nationality = mutableStateOf("")

    private val _status = MutableStateFlow<UiStatus>(UiStatus.Idle)
    val status: StateFlow<UiStatus> = _status
//    private val _status = mutableStateOf<UiStatus>(UiStatus.Idle)
//    val status: StateFlow<UiStatus> = _status()

    fun submitKyc() {
        viewModelScope.launch {
            _status.value = UiStatus.Loading
            try {
                val request = KYCRequest(
                    firstName = firstName.value,
                    lastName = lastName.value,
                    dateOfBirth = dateOfBirth.value,
                    salary = salary.value.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    nationality = nationality.value
                )

                val response: KYCResponse = RetrofitInstance.getBankingServiceProvide(context).updateKyc(request)
                _status.value = UiStatus.Success

            } catch (e: Exception) {
                Log.e("KycViewModel", "Error submitting KYC", e)
                _status.value = UiStatus.Error(e.message ?: "Unknown error")

            }
        }
    }


}

sealed class UiStatus {
    object Idle : UiStatus()
    object Loading : UiStatus()
    object Success : UiStatus()
    data class Error(val message: String) : UiStatus()
}