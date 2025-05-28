package com.coded.loanlift.providers

import com.coded.loanlift.data.response.JwtResponse
import com.coded.loanlift.data.response.LoginRequest
import com.coded.loanlift.data.response.RegisterCreateRequest
import com.coded.loanlift.data.response.UserInfoDto
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthServiceProvider {
    // Auth Controller

    // ROLE -> UNAUTHORIZED
    @POST("/auth/register")
    suspend fun register(
        @Body body: RegisterCreateRequest
    ): Response<JwtResponse>

    // ROLE -> UNAUTHORIZED
    @POST("/auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<JwtResponse>


    // User Controller
    // NOTE Not all controller mappings included

    // ROLE -> ROLE_ADMIN
    @GET("/users/details/{userId}")
    suspend fun getUserDetails(
        @Path("userId") userId: Long
    ): Response<UserInfoDto>

}