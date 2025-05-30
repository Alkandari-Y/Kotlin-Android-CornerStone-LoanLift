package com.coded.loanlift.viewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.coded.loanlift.data.response.auth.LoginRequest
import com.coded.loanlift.data.response.auth.JwtResponse
import com.coded.loanlift.managers.TokenManager
import com.coded.loanlift.providers.RetrofitInstance
import kotlinx.coroutines.launch
import com.coded.loanlift.data.response.auth.JwtContents
import com.coded.loanlift.data.response.auth.RegisterCreateRequest
import com.coded.loanlift.data.response.error.ApiErrorResponse
import com.coded.loanlift.data.response.error.ValidationError
import com.google.gson.Gson
import retrofit2.HttpException


sealed class AuthUiState {
    data object Loading : AuthUiState()
    data class Success(val jwtResponse: JwtResponse) : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}


sealed class AuthResultState {
    data object Idle : AuthResultState()
    data object Loading : AuthResultState()
    data object Success : AuthResultState()
    data class Error(val message: String) : AuthResultState()
}

class AuthViewModel(
    private val context: Context
) : ViewModel() {
    val uiState = mutableStateOf<AuthUiState>(AuthUiState.Loading)
    val registerFieldErrors = mutableStateOf<List<ValidationError>>(emptyList())

    private val authApiService = RetrofitInstance.getAuthServiceProvider(context)
    var token = mutableStateOf<JwtResponse?>(null)
    var decodedToken = mutableStateOf<JwtContents?>(null)

    fun loadStoredToken() {
        val storedToken = TokenManager.getToken(context)
        if (storedToken != null) {
            token.value = storedToken
            decodedToken.value = TokenManager.decodeAccessToken(context)
            Log.d("Token", "Loaded token and decoded: ${decodedToken.value}")
        }
    }

    fun register(username: String, email: String, password: String, civilId: String) {
        viewModelScope.launch {
            uiState.value = AuthUiState.Loading
            try {
                val response = authApiService.register(
                    RegisterCreateRequest(
                        username = username,
                        email = email,
                        password = password,
                        civilId = civilId
                    )
                )
                if (response.isSuccessful) {
                    val jwtResponse = response.body()
                    if (jwtResponse != null) {
                        token.value = jwtResponse
                        TokenManager.saveToken(context, jwtResponse)
                        decodedToken.value = TokenManager.decodeAccessToken(context)
                        uiState.value = AuthUiState.Success(jwtResponse)
                    }
                } else {
                    if (response.code() == 400) {
                        val errorBody = response.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, ApiErrorResponse::class.java)

                        registerFieldErrors.value = errorResponse.fieldErrors ?: emptyList()
                        uiState.value = AuthUiState.Error(errorResponse.message)
                    } else {
                        uiState.value = AuthUiState.Error("Registration failed: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                uiState.value = AuthUiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            uiState.value = AuthUiState.Loading
            try {
                val response = authApiService.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    val jwtResponse = response.body()
                    if (jwtResponse != null) {
                        token.value = jwtResponse
                        TokenManager.saveToken(context, jwtResponse)
                        decodedToken.value = TokenManager.decodeAccessToken(context)
                        uiState.value = AuthUiState.Success(jwtResponse)
                    }
                } else {
                    uiState.value = AuthUiState.Error(
                        when (response.code()) {
                            401 -> "Invalid credentials"
                            500 -> "Server error"
                            else -> "Unknown error"
                        }
                    )
                }
            } catch (e: Exception) {
                uiState.value = AuthUiState.Error("Network error: ${e.message}")
            }
        }
    }

    fun logout() {
        TokenManager.clearToken(context)
        token.value = null
        decodedToken.value = null
        Log.d("Logout", "Token cleared")
    }
}
