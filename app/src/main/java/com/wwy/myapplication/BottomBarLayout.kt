package com.wwy.myapplication

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import java.lang.IllegalArgumentException
import java.util.ArrayList

/**
 *@创建者wwy
 *@创建时间 2019/10/17 14:17
 *@描述
 */
class BottomBarLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {
    private val STATE_INSTANCE = "instance_state"
    private val STATE_ITEM = "state_item"
    private var mSmoothScroll: Boolean = false
    private var mViewPager: ViewPager? = null
    private var mChildCount: Int = 0//子条目个数
    private val mItemViews = ArrayList<BottomBarItem>()
    private var mCurrentItem: Int = 0//当前条目的索引
    private var onItemSelectedListener: ((BottomBarItem, Int, Int) -> Unit)? = null

    init {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.BottomBarLayout)
        mSmoothScroll = obtainStyledAttributes.getBoolean(R.styleable.BottomBarLayout_smoothScroll, false)
        obtainStyledAttributes.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        init()
    }


    fun setViewPager(viewPager: ViewPager) {
        this.mViewPager = viewPager
        init()
    }

    private fun init() {
        mItemViews.clear()
        mChildCount = childCount
        if (0 == mChildCount) {
            return
        }
        mViewPager?.let { require(mViewPager?.adapter?.count == mChildCount) { "LinearLayout的子View数量必须和ViewPager条目数量一致\"" } }

        for (i in 0 until mChildCount) {
            if (getChildAt(i) is BottomBarItem) {
                val bottomBarItem = getChildAt(i) as BottomBarItem
                mItemViews.add(bottomBarItem)
                bottomBarItem.setOnClickListener(MyOnClickListener(i))

            } else {
                throw IllegalArgumentException("BottomBarLayout的子View必须是BottomBarItem")
            }
        }
        //初始化 设置选中的Item状态
        if (mCurrentItem < mItemViews.size) {
            mItemViews[mCurrentItem].refreshTab(true)
        }
        mViewPager?.addOnPageChangeListener(this)
    }

    //inner 设置为非静态内部类 能访问外部类成员变量
    inner class MyOnClickListener(private val currentIndex: Int) : OnClickListener {
        override fun onClick(p0: View?) {
            if (null != mViewPager) {
                //如果点击的条目是选中的
                if (currentIndex == mCurrentItem) {
                    onItemSelectedListener?.invoke(getBottomItem(currentIndex), mCurrentItem, currentIndex)
                    onItemSelectedListener?.let { setOnItemSelectedListener(it) }
                } else {
                    mViewPager?.setCurrentItem(currentIndex, mSmoothScroll)
                }

            } else {//没有设置ViewPage
                onItemSelectedListener?.invoke(getBottomItem(currentIndex), mCurrentItem, currentIndex)
                onItemSelectedListener?.let { setOnItemSelectedListener(it) }
                updateTabState(currentIndex)

            }
        }

    }

    fun setCurrentItem(currentItem: Int) {
        if (null != mViewPager) {
            mViewPager?.setCurrentItem(currentItem, mSmoothScroll)
        } else {
            onItemSelectedListener?.invoke(getBottomItem(currentItem), mCurrentItem, currentItem)
            onItemSelectedListener?.let { setOnItemSelectedListener(it) }
        }
        updateTabState(currentItem)
    }

    fun addItem(item: BottomBarItem) {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams.weight = 1f
        item.layoutParams = layoutParams
        addView(item)
        init()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < mItemViews.size) {
            val item = mItemViews[position]
            if (mItemViews.contains(item)) {
                resetState()
                removeViewAt(position)
                init()
            }
        }
    }


    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
        Log.d("wwywwy","position---$position")
        //重置
        resetState()
        mItemViews[position].refreshTab(true)
        onItemSelectedListener?.invoke(getBottomItem(position), mCurrentItem, position)
        onItemSelectedListener?.let { setOnItemSelectedListener(it) }
        mCurrentItem = position//记录当前位置
    }

    private fun updateTabState(position: Int) {
        resetState()
        mCurrentItem = position
        mItemViews[mCurrentItem].refreshTab(true)
    }

    private fun resetState() {
        if (mCurrentItem < mItemViews.size) {
            Log.d("wwywwy","mCurrentItem---$mCurrentItem")
            mItemViews[mCurrentItem].refreshTab(false)
        }
    }

    /**
     * 设置未读数
     *
     * @param position  底部标签的下标
     * @param unreadNum 未读数
     */
    fun setUnread(position: Int, unreadNum: Int) {
        mItemViews[position].setUnreadNum(unreadNum)
    }

    /**
     * 设置提示消息
     *
     * @param position 底部标签的下标
     * @param msg      未读数
     */
    fun setMsg(position: Int, msg: String) {
        mItemViews[position].setMsg(msg)
    }

    /**
     * 隐藏提示消息
     *
     * @param position 底部标签的下标
     */
    fun hideMsg(position: Int) {
        mItemViews[position].hideMsg()
    }

    /**
     * 显示提示的小红点
     *
     * @param position 底部标签的下标
     */
    fun showNotify(position: Int) {
        mItemViews[position].showNotify()
    }

    /**
     * 隐藏提示的小红点
     *
     * @param position 底部标签的下标
     */
    fun hideNotify(position: Int) {
        mItemViews[position].hideNotify()
    }

    fun setSmoothScroll(smoothScroll: Boolean) {
        this.mSmoothScroll = smoothScroll
    }

     fun getBottomItem(position: Int): BottomBarItem {
        return mItemViews[position]
    }

    /**
     * @return 当View被销毁的时候，保存数据
     */
    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState())
        bundle.putInt(STATE_ITEM, mCurrentItem)
        return bundle
    }

    /**
     * @param state 用于恢复数据使用
     */
    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            mCurrentItem = state.getInt(STATE_ITEM)
            //重置所有按钮状态
            resetState()
            //恢复点击的条目颜色
            mItemViews[mCurrentItem].refreshTab(true)
            super.onRestoreInstanceState(state.getParcelable(STATE_INSTANCE))
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    fun setOnItemSelectedListener(onItemSelectedListener: (bottomBarItem: BottomBarItem, previousPosition: Int, currentPosition: Int) -> Unit) {
        this.onItemSelectedListener = onItemSelectedListener
    }
}