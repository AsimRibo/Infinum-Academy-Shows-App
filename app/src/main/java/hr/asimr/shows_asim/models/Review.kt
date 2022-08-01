package hr.asimr.shows_asim.models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(
    tableName = "reviews", foreignKeys = [ForeignKey(
        entity = Show::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("showId")
    )]
)
data class Review(
    @SerialName("id")
    @PrimaryKey
    @ColumnInfo(name="idReview")
    val id: String,

    @SerialName("comment")
    @ColumnInfo(name="comment")
    val comment: String,

    @SerialName("rating")
    @ColumnInfo(name="rating")
    val rating: Int,

    @SerialName("show_id")
    @ColumnInfo(name="show_id")
    val showId: Int,

    @SerialName("user")
    @Embedded
    val user: User
)