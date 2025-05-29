package com.coded.loanlift.formStates

data class LoginFormState(
    var password: String = "",
    var username: String = "",
    var passwordError: String? = "",
    var usernameError: String? = ""
) {
    val formIsValid: Boolean
        get() = password.isNotBlank() &&
                username.isNotBlank()

    val passwordsValid: Boolean
        get() = password.isNotBlank()

    fun validate(): LoginFormState {
        return this.copy(
            passwordError = if (password.isBlank()) "Password cannot be empty" else null,
            usernameError = if (username.isBlank()) "Userame cannot be empty" else null
        )
    }
}