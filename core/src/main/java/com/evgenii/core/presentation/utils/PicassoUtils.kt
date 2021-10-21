package com.evgenii.core.presentation.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.squareup.picasso.Picasso

class PicassoUtils {

    companion object {
        fun ImageView.loadFromPicasso(url: String, @DrawableRes placeholderImageRes: Int) =
            if (url.isNotEmpty()) {
                Picasso.get()
                    .load(url)
                    .placeholder(placeholderImageRes)
                    .into(this)
            } else {
                this.setImageResource(placeholderImageRes)
            }
    }
}