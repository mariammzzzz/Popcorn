package com.mjapa21.popcorn.domain.validation

object UserDataValidator {
    const val USERNAME_MIN = 3
    const val USERNAME_MAX = 20
    const val PASSWORD_MIN = 8

    private val USERNAME_REGEX = Regex("^[a-z0-9_]+$")

    /** Always normalize before validating or sending to the backend. */
    fun normalizeUsername(raw: String): String = raw.trim().lowercase()

    fun usernameError(raw: String): String? {
        val username = normalizeUsername(raw)
        return when {
            username.length < USERNAME_MIN -> "Username must be at least $USERNAME_MIN characters"
            username.length > USERNAME_MAX -> "Username can be at most $USERNAME_MAX characters"
            !USERNAME_REGEX.matches(username) -> "Only letters, numbers and underscores"
            else -> null
        }
    }

    fun passwordError(password: String): String? = when {
        password.length < PASSWORD_MIN -> "Password must be at least $PASSWORD_MIN characters"
        else -> null
    }
}