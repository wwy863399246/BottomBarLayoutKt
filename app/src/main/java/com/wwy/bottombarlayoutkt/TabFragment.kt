package com.wwy.bottombarlayout

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

/**
 *@创建者wwy
 *@创建时间 2019/10/18 10:59
 *@描述
 */
class TabFragment : Fragment() {
    val CONTENT: String = "content"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        val string = arguments?.getString(CONTENT)
        textView.text = string
        return textView
    }
}