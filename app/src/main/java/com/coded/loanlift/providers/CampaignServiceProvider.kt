package com.coded.loanlift.providers

import com.coded.loanlift.data.response.campaigns.CampaignDetailResponse
import com.coded.loanlift.data.response.campaigns.CampaignListItemResponse
import com.coded.loanlift.data.response.campaigns.CampaignOwnerDetails
import com.coded.loanlift.data.response.campaigns.CampaignPublicDetails
import com.coded.loanlift.data.response.comments.CommentCreateRequest
import com.coded.loanlift.data.response.comments.CommentResponseDto
import com.coded.loanlift.data.response.campaigns.DownloadDto
import com.coded.loanlift.data.response.campaigns.FileDto
import com.coded.loanlift.data.response.pledges.PledgeCreateRequest
import com.coded.loanlift.data.response.pledges.PledgeResultDto
import com.coded.loanlift.data.response.pledges.PledgeTransactionWithDetails
import com.coded.loanlift.data.response.pledges.PledgeWithPledgeTransactionsDto
import com.coded.loanlift.data.response.comments.ReplyCreateRequest
import com.coded.loanlift.data.response.comments.ReplyDto
import com.coded.loanlift.data.response.pledges.UpdatePledgeRequest
import com.coded.loanlift.data.response.pledges.UserPledgeDto
import com.coded.loanlift.data.response.campaigns.CampaignTransactionHistoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface CampaignServiceProvider {

    // Campaign Controller

    @GET("/api/v1/campaigns")
    suspend fun getAllCampaigns(): Response<List<CampaignListItemResponse>>

    @Multipart
    @POST("/api/v1/campaigns")
    suspend fun createCampaign(
        @PartMap campaignFields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part
    ): Response<CampaignPublicDetails>

    @GET("/api/v1/campaigns/details/{campaignId}")
    suspend fun getCampaignDetails(
        @Path("campaignId") campaignId: Long,
        @Query("includeComments") includeComments: Boolean = false
    ): Response<CampaignDetailResponse>

    @GET("/api/v1/campaigns/manage")
    suspend fun getMyCampaigns(): Response<List<CampaignListItemResponse>>

    @GET("/api/v1/campaigns/manage/{campaignId}")
    suspend fun getMyCampaignById(
        @Path("campaignId") campaignId: Long,
    ): Response<CampaignOwnerDetails>

    @DELETE("/api/v1/campaigns/manage/{campaignId}")
    suspend fun deleteCampaign(
        @Path("campaignId") campaignId: Long,
    ): Response<Unit>

    @GET("/api/v1/campaigns/manage/{campaignId}/transactions")
    suspend fun getMyCampaignTransactionsById(
        @Path("campaignId") campaignId: Long,
    ): Response<CampaignTransactionHistoryResponse>

    @Multipart
    @PUT("/api/v1/campaigns/manage/{campaignId}")
    suspend fun updateCampaign(
        @Path("campaignId") campaignId: Long,
        @PartMap updateFields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part image: MultipartBody.Part? = null
    ): Response<CampaignDetailResponse>

    @Multipart
    @POST("/api/v1/campaigns/manage/files")
    suspend fun uploadFile(
        @PartMap fields: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part file: MultipartBody.Part
    ): Response<FileDto>

    @GET("/api/v1/campaigns/manage/files/{fileId}/download")
    suspend fun downloadFile(
        @Path("fileId") fileId: Long,
    ): Response<DownloadDto>


    // Comments Controller

    @GET("/api/v1/comments/campaign/{campaignId}")
    suspend fun allAllCampaignComments(
        @Path("campaignId") campaignId: Long,
    ): Response<List<CommentResponseDto>>

    @POST("/api/v1/comments/campaign/{campaignId}")
    suspend fun addComment(
        @Path("campaignId") campaignId: Long,
        @Body commentRequest: CommentCreateRequest,
    ): Response<CommentResponseDto>

    @DELETE("/api/v1/comments/edit/{commentId}")
    suspend fun deleteComment(
        @Path("commentId") commentId: Long,
    ): Response<Unit>

    @POST("/api/v1/comments/reply")
    suspend fun replyToComment(
        @Body replyCreate: ReplyCreateRequest,
    ): Response<ReplyDto>


    // Pledge Controller

    @GET("/api/v1/pledges")
    suspend fun getAllMyPledges(): Response<List<UserPledgeDto>>

    @POST("/api/v1/pledges")
    suspend fun createPledge(
        @Body request: PledgeCreateRequest,
    ): Response<PledgeResultDto>

    @PUT("/api/v1/pledges/details/{pledgeId}")
    suspend fun updatePledge(
        @Path("pledgeId") pledgeId: Long,
        @Body request: UpdatePledgeRequest,
    ): Response<PledgeResultDto>

    @GET("/api/v1/pledges/details/{pledgeId}")
    suspend fun getPledgeDetails(
        @Path("pledgeId") pledgeId: Long,
    ): Response<PledgeWithPledgeTransactionsDto>

    @GET("/api/v1/pledges/details/{pledgeId}/transactions")
    suspend fun getPledgeDetailedTransactions(
        @Path("pledgeId") pledgeId: Long,
    ): Response<List<PledgeTransactionWithDetails>>

    @DELETE("/api/v1/pledges/details/{pledgeId}")
    suspend fun withdrawPledge(
        @Path("pledgeId") pledgeId: Long,
    ): Response<Unit>
}