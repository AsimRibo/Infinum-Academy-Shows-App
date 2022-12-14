package hr.asimr.shows_asim.models.api.response

import hr.asimr.shows_asim.models.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    @SerialName("user") val user: User
)