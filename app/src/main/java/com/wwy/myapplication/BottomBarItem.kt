package com.wwy.myapplication

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.item_bottom_bar.view.*
import java.util.*

/**
 *@创建者wwy
 *@创建时间 2019/10/17 14:45
 *@描述
 */
class BottomBarItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private var normalIcon: Drawable? = null//普通状态图标的资源id
    private var selectedIcon: Drawable? = null//选中状态图标的资源id
    private var title: String? = null//文本
    private var titleTextSize: Int = 12//文字大小 默认为12sp
    private var titleNormalColor: Int = 0    //描述文本的默认显示颜色
    private var titleSelectedColor: Int = 0  //述文本的默认选中显示颜色
    private var marginTop: Int = 0//文字和图标的距离,默认0dp
    private var openTouchBg: Boolean = false// 是否开启触摸背景，默认关闭
    private var touchDrawable: Drawable? = null//触摸时的背景
    private var iconWidth: Int = 0//图标的宽度
    private var iconHeight: Int = 0//图标的高度
    private var itemPadding: Int = 0//BottomBarItem的padding
    private var unreadTextSize: Int = 10 //未读数默认字体大小10sp
    var unreadNumThreshold: Int = 99//未读数阈值
    private var unreadTextColor: Int = 0//未读数字体颜色
    private var unreadTextBg: Drawable? = drawable(R.drawable.shape_unread)//未读数字体背景
    private var msgTextSize: Int = 6 //消息默认字体大小6sp
    private var msgTextColor: Int = 0//消息文字颜色
    private var msgTextBg: Drawable? = drawable(R.drawable.shape_msg)//消息文字背景
    private var notifyPointBg: Drawable? = drawable(R.drawable.shape_notify_point)//小红点背景

    init {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem)
        normalIcon = obtainStyledAttributes.getDrawable(R.styleable.BottomBarItem_iconNormal)
        selectedIcon = obtainStyledAttributes.getDrawable(R.styleable.BottomBarItem_iconSelected)

        title = obtainStyledAttributes.getString(R.styleable.BottomBarItem_itemText)
        titleTextSize =
            obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomBarItem_itemTextSize, UIUtils().sp2px(context, titleTextSize.toFloat()))

        titleNormalColor = obtainStyledAttributes.getColor(R.styleable.BottomBarItem_textColorNormal, color(R.color.bbl_999999))
        titleSelectedColor =
            obtainStyledAttributes.getColor(R.styleable.BottomBarItem_textColorSelected, color(R.color.bbl_ff0000))

        marginTop = obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomBarItem_itemMarginTop, UIUtils().dip2Px(context, marginTop))

        openTouchBg = obtainStyledAttributes.getBoolean(R.styleable.BottomBarItem_openTouchBg, openTouchBg)
        touchDrawable = obtainStyledAttributes.getDrawable(R.styleable.BottomBarItem_touchDrawable)

        iconWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomBarItem_iconWidth, 0)
        iconHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomBarItem_iconHeight, 0)
        itemPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomBarItem_itemPadding, 0)

        unreadTextSize =
            obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomBarItem_unreadTextSize, UIUtils().sp2px(context, unreadTextSize.toFloat()))
        unreadTextColor = obtainStyledAttributes.getColor(R.styleable.BottomBarItem_unreadTextColor, color(R.color.white))
        unreadTextBg = obtainStyledAttributes.getDrawable(R.styleable.BottomBarItem_unreadTextBg)

        msgTextSize =
            obtainStyledAttributes.getDimensionPixelSize(R.styleable.BottomBarItem_msgTextSize, UIUtils().sp2px(context, msgTextSize.toFloat()))
        msgTextColor = obtainStyledAttributes.getColor(R.styleable.BottomBarItem_msgTextColor, color(R.color.white))
        msgTextBg = obtainStyledAttributes.getDrawable(R.styleable.BottomBarItem_msgTextBg)

        notifyPointBg = obtainStyledAttributes.getDrawable(R.styleable.BottomBarItem_notifyPointBg)

        unreadNumThreshold = obtainStyledAttributes.getInteger(R.styleable.BottomBarItem_unreadThreshold, unreadNumThreshold)
        obtainStyledAttributes.recycle()
        initView()
    }

    private fun initView() {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        View.inflate(context, R.layout.item_bottom_bar, this)
        if (itemPadding != 0) {
            //如果有设置item的padding
            setPadding(itemPadding, itemPadding, itemPadding, itemPadding)
        }
        iv_icon.setImageDrawable(normalIcon)
        if (iconWidth != 0 && iconHeight != 0) {
            //如果有设置图标的宽度和高度，则设置ImageView的宽高
            val imageLayoutParams = iv_icon.layoutParams as FrameLayout.LayoutParams
            imageLayoutParams.width = iconWidth
            imageLayoutParams.height = iconHeight
            iv_icon.layoutParams = imageLayoutParams
        }
        tv_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, titleTextSize.toFloat())//设置底部文字字体大小

        tv_unred_num.setTextSize(TypedValue.COMPLEX_UNIT_PX, unreadTextSize.toFloat())//设置未读数的字体大小
        tv_unred_num.setTextColor(unreadTextColor)//设置未读数字体颜色
        tv_unred_num.background = unreadTextBg//设置未读数背景

        tv_msg.setTextSize(TypedValue.COMPLEX_UNIT_PX, msgTextSize.toFloat())//设置提示文字的字体大小
        tv_msg.setTextColor(msgTextColor)//设置提示文字的字体颜色
        tv_msg.background = msgTextBg//设置提示文字的背景颜色

        tv_point.background = notifyPointBg//设置提示点的背景颜色

        tv_text.setTextColor(titleNormalColor)//设置底部文字字体颜色
        title?.let { tv_text.text = title }//设置标签文字
        val textLayoutParams = tv_text.layoutParams as LayoutParams
        textLayoutParams.topMargin = marginTop
        tv_text.layoutParams = textLayoutParams

        if (openTouchBg) {
            //如果有开启触摸背景
            background = touchDrawable
        }
    }

    fun setNormalIcon(normalIcon: Drawable?) {
        this.normalIcon = normalIcon
        refreshTab()
    }

    fun setNormalIcon(resId: Int) {
        setNormalIcon(drawable(resId))
    }

    fun setSelectedIcon(selectedIcon: Drawable?) {
        this.selectedIcon = selectedIcon
        refreshTab()
    }

    fun setSelectedIcon(resId: Int) {
        setSelectedIcon(drawable(resId))
    }

    fun refreshTab(isSelected: Boolean) {
        setSelected(isSelected)
        refreshTab()
    }

    fun refreshTab() {
        Log.d("wwywwy","isSelected---$isSelected")
        iv_icon.setImageDrawable(if (isSelected) selectedIcon else normalIcon)
        tv_text.setTextColor(if (isSelected) titleSelectedColor else titleNormalColor)
    }

    private fun setTvVisible(tv: TextView) {
        //都设置为不可见
        tv_unred_num.visibility = View.GONE
        tv_msg.visibility = View.GONE
        tv_point.visibility = View.GONE
        tv.visibility = View.VISIBLE//设置为可见
    }

    fun setUnreadNum(unreadNum: Int) {
        setTvVisible(tv_unred_num)
        when {
            unreadNum <= 0 -> tv_unred_num.visibility = View.GONE
            unreadNum <= unreadNumThreshold -> tv_unred_num.text = unreadNum.toString()
            else -> tv_unred_num.text = String.format(Locale.CHINA, "%d+", unreadNumThreshold)
        }
    }

    fun setMsg(msg: String) {
        setTvVisible(tv_msg)
        tv_msg.text = msg
    }

    fun hideMsg() {
        tv_msg.visibility = View.GONE
    }

    fun showNotify() {
        setTvVisible(tv_point)
    }

    fun hideNotify() {
        tv_point.visibility = View.GONE
    }

    fun create(builder: Builder): BottomBarItem {
        this.normalIcon = builder.normalIcon
        this.selectedIcon = builder.selectedIcon
        this.title = builder.title
        this.titleTextSize = builder.titleTextSize
        this.titleNormalColor = builder.titleNormalColor
        this.titleSelectedColor = builder.titleSelectedColor
        this.marginTop = builder.marginTop
        this.openTouchBg = builder.openTouchBg
        this.touchDrawable = builder.touchDrawable
        this.iconWidth = builder.iconWidth
        this.iconHeight = builder.iconHeight
        this.itemPadding = builder.itemPadding
        this.unreadTextSize = builder.unreadTextSize
        this.unreadTextColor = builder.unreadTextColor
        this.unreadTextBg = builder.unreadTextBg
        this.unreadNumThreshold = builder.unreadNumThreshold
        this.msgTextSize = builder.msgTextSize
        this.msgTextColor = builder.msgTextColor
        this.msgTextBg = builder.msgTextBg
        this.notifyPointBg = builder.notifyPointBg
        initView()
        return this
    }

    inner class Builder(private val context: Context) {
        var normalIcon: Drawable? = null//普通状态图标的资源id
        var selectedIcon: Drawable? = null//选中状态图标的资源id
        var title: String? = null//标题
        var titleTextSize: Int = 0//字体大小
        var titleNormalColor: Int = 0    //描述文本的默认显示颜色
        var titleSelectedColor: Int = 0  //述文本的默认选中显示颜色
        var marginTop: Int = 0//文字和图标的距离
        var openTouchBg: Boolean = false// 是否开启触摸背景，默认关闭
        var touchDrawable: Drawable? = null//触摸时的背景
        var iconWidth: Int = 0//图标的宽度
        var iconHeight: Int = 0//图标的高度
        var itemPadding: Int = 0//BottomBarItem的padding
        var unreadTextSize: Int = 0 //未读数字体大小
        var unreadNumThreshold: Int = 0//未读数阈值
        var unreadTextColor: Int = 0//未读数字体颜色
        var unreadTextBg: Drawable? = drawable(R.drawable.shape_unread)//未读数文字背景
        var msgTextSize: Int = 0 //消息字体大小
        var msgTextColor: Int = 0//消息文字颜色
        var msgTextBg: Drawable? = drawable(R.drawable.shape_msg)//消息提醒背景颜色
        var notifyPointBg: Drawable? = drawable(R.drawable.shape_notify_point)//小红点背景颜色

        init {
            titleTextSize = UIUtils().sp2px(context, 12f)
            titleNormalColor = color(R.color.bbl_999999)
            titleSelectedColor = color(R.color.bbl_ff0000)
            unreadTextSize = UIUtils().sp2px(context, 10f)
            msgTextSize = UIUtils().sp2px(context, 6f)
            unreadTextColor = color(R.color.white)
            unreadNumThreshold = 99
            msgTextColor = color(R.color.white)
        }

        /**
         * Sets the default icon's resourceId
         */
        fun normalIcon(normalIcon: Drawable): Builder {
            this.normalIcon = normalIcon
            return this
        }

        /**
         * Sets the selected icon's resourceId
         */
        fun selectedIcon(selectedIcon: Drawable): Builder {
            this.selectedIcon = selectedIcon
            return this
        }

        /**
         * Sets the title's resourceId
         */
        fun title(titleId: Int): Builder {
            this.title = context.getString(titleId)
            return this
        }

        /**
         * Sets the title string
         */
        fun title(title: String): Builder {
            this.title = title
            return this
        }

        /**
         * Sets the title's text size
         */
        fun titleTextSize(titleTextSize: Int): Builder {
            this.titleTextSize = UIUtils().sp2px(context, titleTextSize.toFloat())
            return this
        }

        /**
         * Sets the title's normal color resourceId
         */
        fun titleNormalColor(titleNormalColor: Int): Builder {
            this.titleNormalColor = color(titleNormalColor)
            return this
        }

        /**
         * Sets the title's selected color resourceId
         */
        fun titleSelectedColor(titleSelectedColor: Int): Builder {
            this.titleSelectedColor = color(titleSelectedColor)
            return this
        }

        /**
         * Sets the item's margin top
         */
        fun marginTop(marginTop: Int): Builder {
            this.marginTop = marginTop
            return this
        }

        /**
         * Sets whether to open the touch background effect
         */
        fun openTouchBg(openTouchBg: Boolean): Builder {
            this.openTouchBg = openTouchBg
            return this
        }

        /**
         * Sets touch background
         */
        fun touchDrawable(touchDrawable: Drawable): Builder {
            this.touchDrawable = touchDrawable
            return this
        }

        /**
         * Sets icon's width
         */
        fun iconWidth(iconWidth: Int): Builder {
            this.iconWidth = iconWidth
            return this
        }

        /**
         * Sets icon's height
         */
        fun iconHeight(iconHeight: Int): Builder {
            this.iconHeight = iconHeight
            return this
        }


        /**
         * Sets padding for item
         */
        fun itemPadding(itemPadding: Int): Builder {
            this.itemPadding = itemPadding
            return this
        }

        /**
         * Sets unread font size
         */
        fun unreadTextSize(unreadTextSize: Int): Builder {
            this.unreadTextSize = UIUtils().sp2px(context, unreadTextSize.toFloat())
            return this
        }

        /**
         * Sets the number of unread array thresholds greater than the threshold to be displayed as n + n as the set threshold
         */
        fun unreadNumThreshold(unreadNumThreshold: Int): Builder {
            this.unreadNumThreshold = unreadNumThreshold
            return this
        }

        /**
         * Sets the message font size
         */
        fun msgTextSize(msgTextSize: Int): Builder {
            this.msgTextSize = UIUtils().sp2px(context, msgTextSize.toFloat())
            return this
        }

        /**
         * Sets the message font background
         */
        fun unreadTextBg(unreadTextBg: Drawable): Builder {
            this.unreadTextBg = unreadTextBg
            return this
        }

        /**
         * Sets unread font color
         */
        fun unreadTextColor(unreadTextColor: Int): Builder {
            this.unreadTextColor = color(unreadTextColor)
            return this
        }

        /**
         * Sets the message font color
         */
        fun msgTextColor(msgTextColor: Int): Builder {
            this.msgTextColor = color(msgTextColor)
            return this
        }

        /**
         * Sets the message font background
         */
        fun msgTextBg(msgTextBg: Drawable): Builder {
            this.msgTextBg = msgTextBg
            return this
        }

        /**
         * Set the message prompt point background
         */
        fun notifyPointBg(notifyPointBg: Drawable): Builder {
            this.notifyPointBg = notifyPointBg
            return this
        }

        /**
         * Create a BottomBarItem object
         */
        fun create(normalIcon: Drawable?, selectedIcon: Drawable?, text: String): BottomBarItem {
            this.normalIcon = normalIcon
            this.selectedIcon = selectedIcon
            title = text

            val bottomBarItem = BottomBarItem(context)
            return bottomBarItem.create(this)
        }

        fun create(normalIconId: Int, selectedIconId: Int, text: String): BottomBarItem {
            return create(drawable(normalIconId), drawable(selectedIconId), text)
        }
    }

    //扩展函数
    fun Context.color(colorRes: Int) = ContextCompat.getColor(this, colorRes)

    fun Context.drawable(drawableRes: Int) = ContextCompat.getDrawable(this, drawableRes)

    fun View.color(colorRes: Int) = context.color(colorRes)

    fun View.drawable(drawableRes: Int) = context.drawable(drawableRes)
}