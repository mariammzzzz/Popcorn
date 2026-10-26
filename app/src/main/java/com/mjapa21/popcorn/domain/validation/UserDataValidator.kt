package com.mjapa21.popcorn.domain.validation

object UserDataValidator {
    const val PASSWORD_MIN = 6
    const val EMAIL_MAX = 254

    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    // ---------- Email ----------

    /** Always normalize before validating or sending to the backend. */
    fun normalizeEmail(raw: String): String = raw.trim().lowercase()

    /** Emails never contain spaces, so drop them as the user types or pastes. */
    fun sanitizeEmail(raw: String): String = raw.filterNot { it.isWhitespace() }

    fun emailError(raw: String): String? {
        val email = normalizeEmail(raw)
        return when {
            email.isEmpty() -> "Enter your email"
            email.length > EMAIL_MAX -> "Email is too long"
            !EMAIL_REGEX.matches(email) -> "Enter a valid email address"
            else -> null
        }
    }

    // ---------- Password ----------

    /** Drops whitespace and control characters, e.g. "my pass word " -> "mypassword" */
    fun sanitizePassword(raw: String): String =
        raw.filterNot { it.isWhitespace() || it.isISOControl() }

    fun passwordError(password: String): String? = when {
        password.any { it.isWhitespace() } -> "Password cannot contain whitespace"
        password.length < PASSWORD_MIN -> "Password must be at least $PASSWORD_MIN characters"
        else -> null
    }
}