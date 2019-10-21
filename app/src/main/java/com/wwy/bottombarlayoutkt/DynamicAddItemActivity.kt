package com.wwy.bottombarlayoutkt

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction

import com.wwy.bottombarlayout.BottomBarItem
import com.wwy.bottombarlayout.BottomBarLayout
import com.wwy.bottombarlayout.TabFragment
import kotlinx.android.synthetic.main.activity_view_pager.*

import java.util.ArrayList
import java.util.Random

/**
 *@创建者wwy
 *@创建时间 2019/10/21 14:53
 *@描述 动态添加条目
 */
class DynamicAddItemActivity : AppCompatActivity() {

    private val mFragmentList = arrayListOf<TabFragment>()

    private val mNormalIconIds = intArrayOf(
        R.mipmap.tab_home_normal,
        R.mipmap.tab_video_normal,
        R.mipmap.tab_micro_normal,
        R.mipmap.tab_me_normal
    )

    private val mSelectedIconIds = intArrayOf(
        R.mipmap.tab_home_selected,
        R.mipmap.tab_video_selected,
        R.mipmap.tab_micro_selected,
        R.mipmap.tab_me_selected
    )

    private val mTitleIds =
        intArrayOf(R.string.tab_home, R.string.tab_video, R.string.tab_micro, R.string.tab_me)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dynamic_add_item)

        initView()
        initData()
        initListener()
    }

    private fun initView() {

    }

    private fun initData() {
        for (i in mTitleIds.indices) {
            //创建item
            val item = createBottomBarItem(i)
            bbl.addItem(item)

            val homeFragment = createFragment(mTitleIds[i])
            mFragmentList.add(homeFragment)
        }

        changeFragment(0) //默认显示第一页
    }

    private fun createFragment(titleId: Int): TabFragment {
        val homeFragment = TabFragment()
        val bundle = Bundle()
        bundle.putString(TabFragment().CONTENT, getString(titleId))
        homeFragment.arguments = bundle
        return homeFragment
    }

    private fun createBottomBarItem(i: Int): BottomBarItem {
        return BottomBarItem(this).Builder(this)
            .titleTextSize(8)
            .titleNormalColor(R.color.tab_normal_color)
            .titleSelectedColor(R.color.tab_selected_color)
            //              .openTouchBg(false)
            //              .marginTop(5)
            //              .itemPadding(5)
            //              .unreadNumThreshold(99)
            //              .unreadTextColor(R.color.white)

            //还有很多属性，详情请查看Builder里面的方法
            //There are still many properties, please see the methods in the Builder for details.
            .create(mNormalIconIds[i], mSelectedIconIds[i], getString(mTitleIds[i]))
    }

    private fun initListener() {
        bbl.setOnItemSelectedListener { _, previousPosition, currentPosition ->
            //如果点击了其他条目
            changeFragment(currentPosition)
        }
    }

    private fun changeFragment(currentPosition: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_content, mFragmentList[currentPosition])
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dynamic, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_item -> {
                val random = Random().nextInt(3)
                //addFragment
                mFragmentList.add(createFragment(mTitleIds[random]))
                //addItem
                val bottomBarItem = createBottomBarItem(random)
                bbl.addItem(bottomBarItem)

                bbl.setCurrentItem(mFragmentList.size - 1)
            }
            R.id.action_remove_item -> {
                //移除条目
                bbl.removeItem(0)

                if (mFragmentList.size != 0) {
                    mFragmentList.removeAt(0)

                    if (mFragmentList.size != 0) {
                        bbl.setCurrentItem(0)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
