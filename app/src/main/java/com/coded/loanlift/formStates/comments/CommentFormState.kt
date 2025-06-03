package com.coded.loanlift.formStates.comments

data class CommentFormState(
    var message: String = "",
    var messageError: String? = null
) {
    val isValid: Boolean
        get() = message.length in 3..50

    fun validate(): CommentFormState {
        return this.copy(
            messageError = when {
                message.isBlank() -> "Comment cannot be empty"
                message.length < 3 -> "Comment must be at least 3 characters"
                message.length > 50 -> "Comment must not exceed 50 characters"
                else -> null
            }
        )
    }
}
