package com.time.demo.gntv.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.ViewPager

/**
 *Project:GradientNavigationTextView
 *Author:君
 *Time:2019-02-27 16:27
 */
class NavigationLayout : RelativeLayout {
    var mTitles: Array<GradientNavigationTextView>? = null
    var mPager: ViewPager? = null
    var mTitleWarpper: LinearLayout? = null
    var mScrollWarpper: HorizontalScrollView? = null
    var mNavBar: View? = null
    var mNavBarBackground: View? = null
    var mNavBarWidth: Int = 120
    var mMarginLeft = 0
    var mTitleWidth = 0F
    var mTitleLeftMargin = 0
    var mCount = 0 //标题数量
    var mTitleWidthOffset = 0
    var mCurrentPosition = 0//当前位置

    var onTitleClickedListener: OnTitleClickedListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        mMarginLeft = 0
        mTitleWarpper = LinearLayout(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mTitleWarpper?.layoutParams = params
        mTitleWarpper?.gravity = Gravity.CENTER_VERTICAL
        mTitleWarpper?.orientation = LinearLayout.HORIZONTAL

        val paramsTitle = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mScrollWarpper = HorizontalScrollView(context)
        mScrollWarpper?.addView(mTitleWarpper, paramsTitle)
        mScrollWarpper?.isHorizontalScrollBarEnabled = false

        addView(mScrollWarpper)
    }

    /**
     * 设置导航背景
     */
    fun setNavBarGround(height: Int, color: Int) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, height)
        mNavBarBackground = View(context)
        mNavBarBackground?.layoutParams = params
        mNavBarBackground?.setBackgroundColor(color)
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, height)
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE)
        addView(mNavBarBackground, lp)
    }

    /**
     * 设置导航条
     */
    fun setNavBar(height: Int, color: Int) {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, height)
        mNavBar = View(context)
        mNavBar?.layoutParams = params
        mNavBar?.setBackgroundColor(color)
        val lp = LayoutParams(mNavBarWidth, height)
        lp.addRule(ALIGN_PARENT_BOTTOM, TRUE)
        addView(mNavBar, lp)
        moveBar(mNavBar!!, mNavBarWidth, 0F, 0, mScrollWarpper!!)
    }

    /**
     * 导航条滑动
     */
    private fun moveBar(mNavBar: View, mNavWidth: Int, percent: Float, position: Int, mWarpper: HorizontalScrollView) {
        val left = ((position + percent) * mNavWidth).toInt()
        val lp = mNavBar.layoutParams as LayoutParams
        lp.width = mNavWidth - mTitleWidthOffset * 2
        lp.setMargins(left + mTitleLeftMargin + mTitleWidthOffset, 0, mTitleWidthOffset, 0)
        mNavBar.requestLayout()
    }

    /**
     * 设置标题
     */
    private fun setTitles(
        titles: Array<String>,
        mTextSize: Float,
        mSmoothScroll: Boolean,
        mSplitLineColor: Int,
        mSplitLineWidth: Float,
        mTopOffset: Float,
        mBottomOffset: Float
    ) {
        mTitles = Array(mCount) { GradientNavigationTextView(context) }
        val params = LinearLayout.LayoutParams(
            dp2px(mTitleWidth),
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        params.gravity = Gravity.CENTER
        val lp = LinearLayout.LayoutParams(
            dp2px(mSplitLineWidth),
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(0, dp2px(mTopOffset), 0, dp2px(mBottomOffset))
        for (i in 0 until mCount) {
            val tv = GradientNavigationTextView(context)
            tv.setText(titles[i])
            tv.setTextSize(mTextSize)
            mTitles!![i] = tv
            tv.setOnClickListener {
                if (mCurrentPosition == i) {
                    mScrollWarpper?.smoothScrollTo(i * dp2px(mTitleWidth) - mTitleLeftMargin, 0)
                }
                if (mMarginLeft == 0) {
                    mPager?.setCurrentItem(i, mSmoothScroll)
                } else {
                    mPager?.setCurrentItem(i, false)
                }
                onTitleClickedListener?.onTitleClicked(tv)
            }
            mTitleWarpper?.addView(tv, params)
            if (i < mCount - 1) {
                val mSplitLine = View(context)
                mSplitLine.setBackgroundColor(mSplitLineColor)
                mTitleWarpper?.addView(mSplitLine, lp)
            }
        }
        mTitles!![0].setTextProgress(1F)
    }

    /**
     * 设置ViewPager
     */
    fun setViewPager(
        mViewPager: ViewPager,
        titles: Array<String>,
        mTextSelectedSize: Float,
        mTextUnSelectedSize: Float,
        mTextWidth: Int,
        mTextSize: Float,
        mSmoothScroll: Boolean,
        mSplitLineColor: Int,
        mSplitLineWidth: Float,
        mTopOffset: Float,
        mBottomOffset: Float,
        mTitleWidthOffset: Int
    ) {
        mCount = titles.size
        mPager = mViewPager
        this.mTitleWidthOffset = mTitleWidthOffset
        mNavBarWidth = resources.displayMetrics.widthPixels / mCount
        mTitleWidth = mTextWidth + mSplitLineWidth
        setTitles(titles, mTextSize, mSmoothScroll, mSplitLineColor, mSplitLineWidth, mTopOffset, mBottomOffset)
        setNavBarGround(2, Color.GRAY)
        setNavBar(4, Color.BLUE)
        mPager?.currentItem = 0
        mPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                mTitles?.forEach {
                    it.setTextProgress(0F)
                }
                if (state == ViewPager.SCREEN_STATE_OFF) {
                    mTitles!![mPager!!.currentItem].setTextProgress(1F)
                }
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (positionOffset > 0) {
                    val left = mTitles!![position]
                    val right = mTitles!![position + 1]
                    left.mDirection = GradientNavigationTextView.DIRECTION_RIGHT
                    right.mDirection = GradientNavigationTextView.DIRECTION_LEFT
                    left.setTextProgress(1 - positionOffset)
                    right.setTextProgress(positionOffset)
                }
                mTitleLeftMargin = if (position == 0) (mMarginLeft * positionOffset).toInt() else mMarginLeft
                moveBar(mNavBar!!, mNavBarWidth, positionOffset, position, mScrollWarpper!!)
            }

            override fun onPageSelected(position: Int) {
                mCurrentPosition = position
                mTitleLeftMargin = if (position == 0) 0 else mMarginLeft
                mScrollWarpper?.smoothScrollTo(position * dp2px(mTitleWidth) - mTitleLeftMargin, 0)
            }
        })
    }

    interface OnTitleClickedListener {
        fun onTitleClicked(v: View)
    }

    private fun dp2px(dp: Float): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5F).toInt()
    }
}