package com.wwy.bottombarlayout

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.wwy.bottombarlayoutkt.R
import kotlinx.android.synthetic.main.activity_view_pager.*

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
            val bottomItem = bbl.getBottomItem(3)
        }
        bbl.setUnread(0, 20)//设置第一个页签的未读数为20
        bbl.setUnread(1, 1001)//设置第二个页签的未读数
        bbl.showNotify(2)//设置第三个页签显示提示的小红点
        bbl.setMsg(3, "NEW")//设置第四个页签显示NEW提示文字
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

    }


    internal inner class MyAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_demo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when ( item.itemId) {
            R.id.action_clear_unread -> {
                bbl.setUnread(0, 0)
                bbl.setUnread(1, 0)
            }
            R.id.action_clear_notify -> bbl.hideNotify(2)
            R.id.action_clear_msg -> bbl.hideMsg(3)
        }
        return super.onOptionsItemSelected(item)
    }
}