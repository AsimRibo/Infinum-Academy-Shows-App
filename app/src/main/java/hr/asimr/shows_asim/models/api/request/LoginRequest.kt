package hr.asimr.shows_asim.models.api.request

import kotlinx.serialization.SerialName

data class LoginRequest (
    @SerialName("email") val email: String,
    @SerialName("password") val password: String
)