package com.example.authorization

import java.util.regex.Matcher
import java.util.regex.Pattern

class PasswordValidator {
    private val PASSWORD_PATTERN =
        "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,20}$"

    private val pattern: Pattern = Pattern.compile(PASSWORD_PATTERN)

    fun isValid(password: String?): Boolean {
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}
