package com.coded.loanlift.providers

import com.coded.loanlift.data.response.CampaignDetailResponse
import com.coded.loanlift.data.response.CampaignListItemResponse
import com.coded.loanlift.data.response.CampaignOwnerDetails
import com.coded.loanlift.data.response.CampaignStatus
import com.coded.loanlift.data.response.CommentCreateRequest
import com.coded.loanlift.data.response.CommentResponseDto
import com.coded.loanlift.data.response.DownloadDto
import com.coded.loanlift.data.response.ReplyCreateRequest
import com.coded.loanlift.data.response.ReplyDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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
    // NOTE: Need to either manually create form data structure
    // or use json and use
//    @PostMapping(consumes = ["multipart/form-data"])
//    suspend fun createCampaign(
//        @Valid @ModelAttribute campaignCreateRequest: CreateCampaignDto,
//        @RequestPart("image", required = true) image: MultipartFile,
//        @RequestAttribute("authUser") authUser: UserInfoDto,
//    ): ResponseEntity<CampaignEntity>


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


    // NOTE: Need to either manually create form data structure
    // or use json and use
//    @PutMapping("/manage/{campaignId}",
//        consumes = ["multipart/form-data"]
//    )
//    fun updateCampaign(
//        @PathVariable campaignId: Long,
//        @Valid @ModelAttribute campaignUpdateRequest: UpdateCampaignRequest,
//        @RequestPart("image", required = false) image: MultipartFile?,
//        @RequestAttribute("authUser") authUser: UserInfoDto,
//    ): ResponseEntity<CampaignEntity>

    // NOTE: Need to change like form-data issue
//    @POST("/manage/files")
//    fun uploadFile(
//        @Valid @ModelAttribute fileUploadRequest: FileUploadRequest,
//        @RequestPart("file", required = true) file: MultipartFile,
//        @RequestAttribute("authUser") authUser: UserInfoDto,
//    ): ResponseEntity<FileDto>

    // ROLE -> ROLE_USER ()
    @GET("/campaigns/manage/files/{fileId}/download")
    fun downloadFile(
        @Path("fileId") fileId: Long,
    ): Response<DownloadDto>



    // Comments Controller

    // ROLE -> Authenticated User
    @GET("/comments/campaign/{campaignId}")
    fun allAllCampaignComments(
        @Path("campaignId") campaignId: Long,
    ) : List<CommentResponseDto>

    // ROLE -> Authenticated User
    @POST("/comments/campaign/{campaignId}")
    fun addComment(
        @Path("campaignId") campaignId: Long,
        @Body commentRequest: CommentCreateRequest,
    ): Response<CommentResponseDto>


    // ROLE -> ROLE_ADMIN | ROLE_USER (comment owner)
    @DELETE("/comments/edit/{commentId}")
    fun deleteComment(
        @Path("commentId") commentId: Long,
    ): Response<Unit>

    // ROLE -> ROLE_USER (campaign owner)
    @POST("/comments/reply")
    fun replyToComment(
        @Body replyCreate: ReplyCreateRequest,
    ): Response<ReplyDto>
}