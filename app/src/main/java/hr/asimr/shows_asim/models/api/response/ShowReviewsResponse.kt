package hr.asimr.shows_asim.models.api.response

import hr.asimr.shows_asim.models.Review
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShowReviewsResponse(
    @SerialName("reviews") val reviews: MutableList<Review>
)