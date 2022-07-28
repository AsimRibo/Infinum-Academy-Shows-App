package hr.asimr.shows_asim.models.api.response

import hr.asimr.shows_asim.models.Show
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowsResponse(
    @SerialName("shows") val shows: List<Show>
)