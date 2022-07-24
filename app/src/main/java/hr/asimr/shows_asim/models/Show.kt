package hr.asimr.shows_asim.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Show(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val imageResourceId: Int,
    val reviews: List<Review>
) : Parcelable