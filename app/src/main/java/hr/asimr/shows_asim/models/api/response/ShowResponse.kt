package hr.asimr.shows_asim.models.api.response

import hr.asimr.shows_asim.models.Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowResponse(
    @SerialName("show") val show: Show
)