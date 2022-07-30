package hr.asimr.shows_asim.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

fun ImageView.loadImageFrom(url: String?, @DrawableRes placeholderImageId: Int ) {
    Glide
        .with(this)
        .load(url)
        .placeholder(placeholderImageId)
        .signature(ObjectKey(System.currentTimeMillis()))
        .centerCrop()
        .into(this)
}
