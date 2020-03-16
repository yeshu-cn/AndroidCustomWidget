/*
 *
 *  * Copyright (c) 2018 yeshu. All Rights Reserved.
 *
 */

package work.yeshu.widget.colorsbar

import android.content.Context
import androidx.core.content.ContextCompat


object ColorsBarDataUtil {

    fun getStressBarData(context: Context): ColorsBarData {
        val rangeValues = mutableListOf(1, 40, 60, 80, 100)
        val rangeColors = listOf(
            ContextCompat.getColor(context, R.color.color_relax),
            ContextCompat.getColor(context, R.color.color_normal),
            ContextCompat.getColor(context, R.color.color_medium),
            ContextCompat.getColor(context, R.color.color_high)
        )
        return ColorsBarData(rangeValues, rangeColors)
    }
}