package com.wwy.myapplication

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : ListActivity() {
    private val mDatas = arrayOf(DemoBean2("UseWithViewPager", ViewPageActivity::class.java))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val adapter = ArrayAdapter<DemoBean2>(this, android.R.layout.simple_list_item_1, mDatas)
        listAdapter = adapter
    }

    override fun onListItemClick(l: ListView?, v: View?, position: Int, id: Long) {
        val demoBean = mDatas[position]
        startActivity(Intent(this, demoBean.clazz))
    }
}
