package hr.asimr.shows_asim.utils

private val EMAIL_REGEX by lazy { "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex() }

private val DOMAIN_START by lazy { "@" }

fun isEmailValid(email: String) = EMAIL_REGEX.matches(email)

fun loseEmailDomain(email: String) = email.subSequence(0, email.indexOf(DOMAIN_START)).toString()