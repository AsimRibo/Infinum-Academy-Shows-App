package hr.asimr.shows_asim.models

import android.os.Parcelable
import androidx.annotation.DrawableRes


class Show(
    val id: String,
    val name: String,
    val description: String,
    @DrawableRes val imageResourceId: Int,
    val reviews: MutableList<Review>
)