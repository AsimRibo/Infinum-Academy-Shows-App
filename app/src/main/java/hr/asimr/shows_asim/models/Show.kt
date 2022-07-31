package hr.asimr.shows_asim.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "shows")
data class Show(
    @SerialName("id")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @SerialName("average_rating")
    @ColumnInfo(name = "average_rating")
    val averageRating: Float?,

    @SerialName("description")
    @ColumnInfo(name = "description")
    val description: String?,

    @SerialName("image_url")
    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @SerialName("no_of_reviews")
    @ColumnInfo(name = "no_of_reviews")
    val numOfReviews: Int,

    @SerialName("title")
    @ColumnInfo(name = "title")
    val title: String
)