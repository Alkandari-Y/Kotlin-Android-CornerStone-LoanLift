package com.coded.loanlift.providers

import com.coded.loanlift.data.response.CampaignDetailResponse
import com.coded.loanlift.data.response.CampaignListItemResponse
import com.coded.loanlift.data.response.CampaignOwnerDetails
import com.coded.loanlift.data.response.CampaignStatus
import com.coded.loanlift.data.response.CommentCreateRequest
import com.coded.loanlift.data.response.CommentResponseDto
import com.coded.loanlift.data.response.DownloadDto
import com.coded.loanlift.data.response.FileDto
import com.coded.loanlift.data.response.PledgeCreateRequest
import com.coded.loanlift.data.response.PledgeResultDto
import com.coded.loanlift.data.response.PledgeTransactionWithDetails
import com.coded.loanlift.data.response.PledgeWithPledgeTransactionsDto
import com.coded.loanlift.data.response.ReplyCreateRequest
import com.coded.loanlift.data.response.ReplyDto
import com.coded.loanlift.data.response.UpdateCampaignRequest
import com.coded.loanlift.data.response.UpdatePledgeRequest
import com.coded.loanlift.data.response.UserPledgeDto
import com.coded.loanlift.data.response.campaigns.CampaignTransactionHistoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface CampaignServiceProvider {
    // Campaign Controller

    // ROLE -> Authenticated User
    @GET("/campaigns")
    suspend fun getAllCampaigns(
        @Query("status") status: CampaignStatus?
    ): Response<List<CampaignListItemResponse>>


    // ROLE -> ROLE_USER
    @Multipart
    @POST("/campaigns")
    suspend fun createCampaign(
        @PartMap campaignFields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ): Response<CampaignDetailResponse>


    // ROLE -> ROLE_USER
    @GET("/campaigns/details/{campaignId}")
    suspend fun getCampaignDetails(
        @Path("campaignId") campaignId: Long,
        @Query("includeComments") includeComments: Boolean = false
    ): CampaignDetailResponse

    // ROLE -> ROLE_USER
    @GET("/campaigns/manage")
    suspend fun getMyCampaigns()
            : List<CampaignListItemResponse>

    // ROLE -> ROLE_USER (campaign owner)
    @GET("/campaigns/manage/{campaignId}")
    suspend fun getMyCampaignById(
        @Path("campaignId") campaignId: Long,
    ): CampaignOwnerDetails


    // ROLE -> ROLE_USER (campaign owner)
    @DELETE("/campaigns/manage/{campaignId}")
    fun deleteCampaign(
        @Path("campaignId") campaignId: Long,
    ): Response<Unit>

    // ROLE -> ROLE_USER (campaign owner)
    @GET("/campaigns/manage/{campaignId}/transactions")
    fun getMyCampaignTransactionsById(
        @Path("campaignId") campaignId: Long,
    ): Response<CampaignTransactionHistoryResponse>


    // ROLE -> ROLE_USER (campaign owner)
    @Multipart
    @PUT("/campaigns/manage/{campaignId}")
    suspend fun updateCampaign(
        @Path("campaignId") campaignId: Long,
        @PartMap updateFields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part? = null
    ): Response<CampaignDetailResponse>


    // ROLE -> ROLE_USER (campaign owner)
    @Multipart
     @POST("/manage/files")
     fun uploadFile(
         @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
         @Part file: MultipartBody.Part
     ): Response<FileDto>


    // ROLE -> ROLE_USER ()
    @GET("/campaigns/manage/files/{fileId}/download")
    fun downloadFile(
        @Path("fileId") fileId: Long,
    ): Response<DownloadDto>


    // Comments Controller

    // ROLE -> Authenticated User
    @GET("/comments/campaign/{campaignId}")
    suspend fun allAllCampaignComments(
        @Path("campaignId") campaignId: Long,
    ): List<CommentResponseDto>

    // ROLE -> Authenticated User
    @POST("/comments/campaign/{campaignId}")
    suspend fun addComment(
        @Path("campaignId") campaignId: Long,
        @Body commentRequest: CommentCreateRequest,
    ): Response<CommentResponseDto>


    // ROLE -> ROLE_ADMIN | ROLE_USER (comment owner)
    @DELETE("/comments/edit/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: Long,
    ): Response<Unit>


    // ROLE -> ROLE_USER (campaign owner)
    @POST("/comments/reply")
    suspend fun replyToComment(
        @Body replyCreate: ReplyCreateRequest,
    ): Response<ReplyDto>


    // Pledge Controller

    // ROLE -> ROLE_USER (campaign owner)
    @GET("/pledges")
    suspend fun getAllMyPledges(): List<UserPledgeDto>

    // ROLE -> ROLE_USER (campaign owner)
    @POST("/pledges")
    fun createPledge(
        @Body request: PledgeCreateRequest,
    ): Response<PledgeResultDto>


    // ROLE -> ROLE_USER (pledge owner)
    @PUT("/pledges/details/{pledgeId}")
    suspend fun updatePledge(
        @Path("pledgeId") pledgeId: Long,
        @Body request: UpdatePledgeRequest,
    ): Response<PledgeResultDto>

    // ROLE ->  ROLE_ADMIN | ROLE_USER (pledge owner)
    @GET("/pledges/details/{pledgeId}")
    suspend fun getPledgeDetails(
        @Path("pledgeId") pledgeId: Long,
    ): Response<PledgeWithPledgeTransactionsDto>


    // ROLE -> ROLE_ADMIN | ROLE_USER (pledge owner)
    @GET("/pledges/details/{pledgeId}/transactions")
    suspend fun getPledgeDetailedTransactions(
        @Path("pledgeId") pledgeId: Long,
    ): Response<List<PledgeTransactionWithDetails>>

    // ROLE -> ROLE_USER (pledge owner)
    @DELETE("/pledges/details/{pledgeId}")
    suspend fun withdrawPledge(
        @Path("pledgeId") pledgeId: Long,
    ): Response<Unit>
}