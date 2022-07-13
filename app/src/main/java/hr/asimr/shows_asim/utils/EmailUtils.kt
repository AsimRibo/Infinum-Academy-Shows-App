package hr.asimr.shows_asim.utils

private val EMAIL_REGEX by lazy { "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$".toRegex() }

private val DOMAIN_START by lazy { "@" }

fun String.isEmailValid() = EMAIL_REGEX.matches(this)

fun String.loseEmailDomain() = this.subSequence(0, this.indexOf(DOMAIN_START)).toString()