package hr.asimr.shows_asim.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val id: String,
    val rating: Int,
    val comment: String,
    val username: String
) : Parcelable