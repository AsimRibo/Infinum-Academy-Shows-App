package hr.asimr.shows_asim.models

data class Review(
    val id: String,
    val rating: Int,
    val comment: String,
    val username: String
)