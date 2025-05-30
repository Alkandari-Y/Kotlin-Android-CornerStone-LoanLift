package com.coded.loanlift.providers

import com.coded.loanlift.data.response.accounts.AccountCreateRequest
import com.coded.loanlift.data.response.accounts.AccountResponse
import com.coded.loanlift.data.response.category.CategoryDto
import com.coded.loanlift.data.response.category.CategoryRequest
import com.coded.loanlift.data.response.transaction.TransactionDetails
import com.coded.loanlift.data.response.transaction.TransferCreateRequest
import com.coded.loanlift.data.response.accounts.UpdateAccountRequest
import com.coded.loanlift.data.response.accounts.UpdatedBalanceResponse
import com.coded.loanlift.data.response.accounts.UserAccountsResponse
import com.coded.loanlift.data.response.kyc.KYCResponse
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface BankingServiceProvider {

    // Accounts Controller

    @GET("/api/v1/accounts")
    suspend fun getAllAccounts(): Response<List<AccountResponse>>

    @POST("/api/v1/accounts")
    suspend fun createAccount(
        @Body accountCreateRequestDto: AccountCreateRequest,
    ): Response<AccountResponse>

    @POST("/api/v1/accounts/transfer")
    suspend fun transfer(
        @Body transferCreateRequestDto: TransferCreateRequest
    ): Response<UpdatedBalanceResponse>

    @DELETE("/api/v1/accounts/close/{accountNumber}")
    suspend fun closeAccount(
        @Path("accountNumber") accountNumber: String,
    ): Response<Unit>

    @PUT("/api/v1/accounts/details/{accountNumber}")
    suspend fun updateAccount(
        @Path("accountNumber") accountNumber: String,
        @Body accountUpdate: UpdateAccountRequest,
    ): Response<AccountResponse>

    @GET("/api/v1/accounts/details/{accountNumber}")
    suspend fun getAccountDetails(
        @Path("accountNumber") accountNumber: String,
    ): Response<AccountResponse>

    @GET("/api/v1/accounts/clients/{clientId}")
    suspend fun getUserAccounts(
        @Path("clientId") clientId: Long
    ): Response<UserAccountsResponse>

    @GET("/api/v1/accounts/clients")
    suspend fun getAccountDetails(
        @Query("accountId") accountId: Long? = null,
        @Query("accountNumber") accountNumber: String? = null
    ): Response<AccountResponse>


    // Categories Controller

    @GET("/api/v1/categories")
    suspend fun getAllCategories(): Response<List<CategoryDto>>

    @POST("/api/v1/categories")
    suspend fun createNewCategory(
        @Body categoryRequest: CategoryRequest
    ): Response<CategoryDto>


    // Transactions Controller

    @GET("/api/v1/transactions/account/{accountNumber}")
    suspend fun getAllTransactionsByAccountNumber(
        @Path("accountNumber") accountNumber: String,
    ): Response<List<TransactionDetails>>

    @GET("/api/v1/transactions/clients/{clientId}")
    suspend fun getTransactionsByClientId(
        @Path("clientId") clientId: Long,
    ): Response<List<TransactionDetails>>


    // KYC

    @GET("/api/v1/kyc")
    suspend fun getUserKyc(): Response<KYCResponse>

}
