package com.vonbrank.forkify.utils

import android.content.Context
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import com.vonbrank.forkify.ForkifyApplication
import com.vonbrank.forkify.R

fun setImageViewThemeColorFilter(imageView: ImageView) {
    val color =
        ColorUtils.setAlphaComponent(
            ForkifyApplication.context.resources.getColor(R.color.colorPrimary),
            144
        )
    imageView.setColorFilter(color, PorterDuff.Mode.LIGHTEN)
}