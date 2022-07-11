package hr.asimr.shows_asim.utils

private val EMAIL_REGEX by lazy { "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex() }

fun isEmailValid(email: String) = EMAIL_REGEX.matches(email)