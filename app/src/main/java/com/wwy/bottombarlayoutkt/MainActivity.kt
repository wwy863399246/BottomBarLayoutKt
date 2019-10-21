package com.wwy.bottombarlayoutkt

import android.R
import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import com.wwy.bottombarlayout.ViewPageActivity

class MainActivity : ListActivity() {
    private val mDatas = arrayOf(
        DemoBean("UseWithViewPager", ViewPageActivity::class.java),
        DemoBean("UseWithoutViewPager", FragmentManagerActivity::class.java),
        DemoBean("DynamicAddItem", DynamicAddItemActivity::class.java)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = ArrayAdapter<DemoBean>(this, R.layout.simple_list_item_1, mDatas)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        val demoBean = mDatas[position]
        startActivity(Intent(this, demoBean.clazz))
    }
}
