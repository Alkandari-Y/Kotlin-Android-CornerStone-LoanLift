package com.coded.loanlift.utils

import android.content.Context
import com.coded.loanlift.formStates.campaigns.CampaignFormState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

fun prepareCampaignFormParts(
    form: CampaignFormState,
    context: Context
): Pair<Map<String, RequestBody>, MultipartBody.Part> {
    val map = mutableMapOf<String, RequestBody>()

    map["title"] = form.title.toRequestBody()
    map["description"] = form.description.toRequestBody()
    map["goalAmount"] = form.goalAmount.toRequestBody()
    map["campaignDeadline"] = form.campaignDeadline.toRequestBody()
    map["categoryId"] = form.categoryId.toString().toRequestBody()

    val imageFile = form.imageFile!!

    val guessedMime = when {
        imageFile.name.endsWith(".png") -> "image/png"
        imageFile.name.endsWith(".webp") -> "image/webp"
        else -> "image/jpeg" // default/fallback
    }

    val imageRequestBody = imageFile.asRequestBody(guessedMime.toMediaTypeOrNull())
    val imagePart = MultipartBody.Part.createFormData(
        "image",
        imageFile.name,
        imageRequestBody
    )

    return Pair(map, imagePart)
}
