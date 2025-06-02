package com.coded.loanlift.formStates.campaigns

import java.io.File
import java.math.BigDecimal

data class CampaignFormState(
    val categoryId: Long? = null,
    val title: String = "",
    val description: String = "",
    val goalAmount: String = "",
    val campaignDeadline: String = "",
    val imageFile: File? = null,

    val titleError: String? = null,
    val descriptionError: String? = null,
    val goalAmountError: String? = null,
    val campaignDeadlineError: String? = null,
    val categoryIdError: String? = null,
    val imageError: String? = null
) {
    val isValid: Boolean
        get() = listOfNotNull(
            titleError, descriptionError, goalAmountError,
            campaignDeadlineError, categoryIdError, imageError
        ).isEmpty()

    fun validate(): CampaignFormState {
        return this.copy(
            titleError = if (title.isBlank()) "Title required" else null,
            descriptionError = if (description.isBlank()) "Description required" else null,
            goalAmountError = if (goalAmount.toBigDecimalOrNull() == null) "Invalid amount" else null,
            campaignDeadlineError = if (campaignDeadline.isBlank()) "Deadline required" else null,
            categoryIdError = if (categoryId == null) "Category required" else null,
            imageError = if (imageFile == null) "Image required" else null,
        )
    }
}
