package com.coded.loanlift.providers

import com.coded.loanlift.data.response.AccountCreateRequest
import com.coded.loanlift.data.response.AccountResponse
import com.coded.loanlift.data.response.CategoryDto
import com.coded.loanlift.data.response.CategoryRequest
import com.coded.loanlift.data.response.TransactionDetails
import com.coded.loanlift.data.response.TransferCreateRequest
import com.coded.loanlift.data.response.UpdateAccountRequest
import com.coded.loanlift.data.response.UpdatedBalanceResponse
import com.coded.loanlift.data.response.banking.UserAccountsResponse
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

    // ROLE -> Authenticated User
    @GET("/accounts")
    suspend fun getAllAccounts(): Response<List<AccountResponse>>

    // ROLE -> Authenticated User
    @POST("/accounts")
    suspend fun createAccount(
        @Body accountCreateRequestDto: AccountCreateRequest,
    ) : Response<AccountResponse>

    // ROLE -> Authenticated User ("account owner")
    @POST("/accounts/transfer")
    suspend fun transfer(
        @Body transferCreateRequestDto: TransferCreateRequest
    ): Response<UpdatedBalanceResponse>

    // ROLE -> Authenticated User ("account owner")
    @DELETE("/accounts/close/{accountNumber}")
    suspend fun closeAccount(
        @Path("accountNumber") accountNumber : String,
    ): Response<Unit>

    // ROLE -> Authenticated User  ("account owner")
    @PUT("/accounts/details/{accountNumber}")
    suspend fun updateAccount(
        @Path("accountNumber") accountNumber : String,
        @Body accountUpdate: UpdateAccountRequest,
    ): Response<AccountResponse>

    // ROLE -> ROLE_ADMIN | ROLE_USER  ("account owner")
    @GET("/accounts/details/{accountNumber}")
    suspend fun getAccountDetails(
        @Path("accountNumber")accountNumber : String,
    ): Response<AccountResponse>


    // ROLE -> ROLE_ADMIN
    @GET("/accounts/clients/{clientId}")
    suspend fun getUserAccounts(
      @Path("clientId") clientId: Long
    ): Response<UserAccountsResponse>

    // ROLE -> ROLE_ADMIN | ROLE_USER  ("account owner")
    @GET("/accounts/clients")
    suspend fun getAccountDetails(
        @Query("accountId") accountId: Long? = null,
        @Query("accountNumber") accountNumber: String? = null
    ): Response<AccountResponse>


    // Categories Controller

    // ROLE -> Authenticated
    @GET("/categories")
    suspend fun getAllCategories(): Response<List<CategoryDto>>

    // ROLE -> ROLE_ADMIN
    @POST("categories")
    suspend fun createNewCategory(
        @Body categoryRequest: CategoryRequest
    ): Response<CategoryDto>

    // Transactions Controller

    // ROLE -> ROLE_ADMIN | ROLE_USER ("account owner")
    @GET("/transactions/account/{accountNumber}")
    suspend fun getAllTransactionsByAccountNumber(
        @Path("accountNumber") accountNumber: String,
    ): Response<List<TransactionDetails>>


    // ROLE -> ROLE_ADMIN | ROLE_USER ("account owner")
    @GET("/transactions/clients/{clientId}")
    suspend fun getTransactionsByClientId(
        @Path("clientId") clientId: Long,
    ): Response<List<TransactionDetails>>
}
