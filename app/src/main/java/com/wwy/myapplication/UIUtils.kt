package com.wwy.myapplication

import android.content.Context
import androidx.core.content.ContextCompat

/**
 *@创建者wwy
 *@创建时间 2019/10/17 14:11
 *@描述
 */
class UIUtils {
    /**
     * dip-->px
     */
    fun dip2Px(context: Context, dip: Int): Int {
        // px/dip = density;
        // density = dpi/160
        // 320*480 density = 1 1px = 1dp
        // 1280*720 density = 2 2px = 1dp
        val density = context.resources.displayMetrics.density
        return (dip * density + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
     fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun getColor(context: Context, colorId: Int): Int {
        return ContextCompat.getColor(context,colorId)
    }
}