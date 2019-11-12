package com.wwy.bottombarlayoutkt

import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import com.wwy.bottombarlayout.BottomBarItem
import com.wwy.bottombarlayout.TabFragment
import kotlinx.android.synthetic.main.activity_fragment_manager.*

/**
 *@创建者wwy
 *@创建时间 2019/10/21 14:53
 *@描述
 */
class FragmentManagerActivity : AppCompatActivity() {
    private val mFragmentList = arrayListOf<TabFragment>()
    private var mRotateAnimation: RotateAnimation? = null
    private val mHandler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_manager)

        initView()
        initData()
        initListener()
    }

    private fun initListener() {
        bbl.setOnItemSelectedListener { bottomBarItem, previousPosition, currentPosition ->
            changeFragment(currentPosition)
            if (currentPosition == 0) {
                //如果是第一个，即首页
                if (previousPosition == currentPosition) {
                    //如果是在原来位置上点击,更换首页图标并播放旋转动画
                    if (mRotateAnimation != null && !mRotateAnimation?.hasEnded()!!) {
                        //如果当前动画正在执行
                        return@setOnItemSelectedListener
                    }

                    bottomBarItem.setSelectedIcon(R.mipmap.tab_loading)//更换成加载图标 setResId

                    //播放旋转动画
                    if (mRotateAnimation == null) {
                        mRotateAnimation = RotateAnimation(
                            0f, 360f,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                            0.5f
                        )
                        mRotateAnimation?.duration = 800
                        mRotateAnimation?.repeatCount = -1
                    }
                    val bottomImageView = bottomBarItem.getImageView()
                    bottomImageView.animation = mRotateAnimation
                    bottomImageView.startAnimation(mRotateAnimation)//播放旋转动画

                    //模拟数据刷新完毕
                    mHandler.postDelayed(Runnable {
                            bbl.getCurrentItem() === currentPosition //是否还停留在当前页签
                        bottomBarItem.setSelectedIcon(R.mipmap.tab_home_selected)//更换成首页原来选中图标
                        cancelTabLoading(bottomBarItem)
                    }, 3000)
                    return@setOnItemSelectedListener
                }
            }
            //如果点击了其他条目
            val bottomItem = bbl.getBottomItem(0)
            bottomItem.setSelectedIcon(R.mipmap.tab_home_selected)//更换为原来的图标
            cancelTabLoading(bottomItem)//停止旋转动画
        }
    }
    /**
     * 停止首页页签的旋转动画
     */
    private fun cancelTabLoading(bottomItem: BottomBarItem) {
        bottomItem.getImageView().animation?.cancel()
    }
    private fun initData() {
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

        changeFragment(0) //默认显示第一页

    }

    private fun initView() {


    }

    private fun changeFragment(currentPosition: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl_content, mFragmentList[currentPosition])
        transaction.commit()
    }

}