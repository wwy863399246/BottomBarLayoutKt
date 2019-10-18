package com.wwy.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_view_pager.*
import java.util.ArrayList

/**
 *@创建者wwy
 *@创建时间 2019/10/17 20:34
 *@描述
 */
class ViewPageActivity : AppCompatActivity() {
    val mFragmentList = arrayListOf<TabFragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)
        initView()
        initData()
        initListener()
    }

    private fun initListener() {
        bbl.setOnItemSelectedListener { _, _, _ ->
            //如果点击了其他条目
//            val bottomItem = bbl.getBottomItem(0)
//            bottomItem.setSelectedIcon(R.mipmap.tab_home_selected)//更换为原来的图标
        }
    }

    private fun initData() {

        vp_content.adapter = MyAdapter(supportFragmentManager)
        bbl.setViewPager(vp_content)
    }

    private fun initView() {
        val homeFragment = TabFragment()
        val bundle1 = Bundle()
        bundle1.putString(TabFragment().CONTENT, "首页")
        homeFragment.arguments = bundle1
        mFragmentList.add(homeFragment)

        val videoFragment = TabFragment()
        val bundle2 = Bundle()
        bundle2.putString(TabFragment().CONTENT, "视频")
        videoFragment.arguments = bundle2
        mFragmentList.add(videoFragment)

        val microFragment = TabFragment()
        val bundle3 = Bundle()
        bundle3.putString(TabFragment().CONTENT, "微头条")
        microFragment.arguments = bundle3
        mFragmentList.add(microFragment)

        val meFragment = TabFragment()
        val bundle4 = Bundle()
        bundle4.putString(TabFragment().CONTENT, "我的")
        meFragment.arguments = bundle4
        mFragmentList.add(meFragment)
        Log.d("wwywwy", mFragmentList.size.toString())

    }


    internal inner class MyAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }
    }
}