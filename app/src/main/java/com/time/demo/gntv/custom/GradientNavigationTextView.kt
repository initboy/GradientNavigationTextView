package com.time.demo.gntv.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.time.demo.gntv.R

/**
 *Project:GradientNavigationTextView
 *Author:君
 *Time:2019-02-27 14:23
 */
class GradientNavigationTextView : View {
    companion object {
        const val DIRECTION_LEFT = 0
        const val DIRECTION_RIGHT = 1
        const val DIRECTION_TOP = 2
        const val DIRECTION_BOTTOM = 3
    }

    var mDirection = DIRECTION_LEFT
    var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    var mTextSize: Int = sp2px(20F)
    var mTextBound: Rect = Rect()
    var mTextNormalColor: Int = Color.GRAY
    var mTextFocusColor: Int = Color.BLUE
    var mTextWidth: Int = 120
    var mTextHeight: Int = 0
    var mTextProgress: Float = 0F
    var mText: String? = ""
    var mTextStartX: Int = 0
    var mTextStartY: Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        val mTypedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.GradientNavigationTextView
        )
        mText = mTypedArray.getString(R.styleable.GradientNavigationTextView_text)
        mTextSize = mTypedArray.getDimensionPixelSize(
            R.styleable.GradientNavigationTextView_text_size,
            mTextSize
        )
        mTextNormalColor = mTypedArray.getColor(
            R.styleable.GradientNavigationTextView_text_normal_color,
            mTextNormalColor
        )
        mTextFocusColor = mTypedArray.getColor(
            R.styleable.GradientNavigationTextView_text_focus_color,
            mTextFocusColor
        )
        mDirection = mTypedArray.getInt(R.styleable.GradientNavigationTextView_direction, mDirection)
        mTextProgress = mTypedArray.getFloat(R.styleable.GradientNavigationTextView_progress, mTextProgress)
        mTypedArray.recycle()
        mPaint.textSize = mTextSize.toFloat()
    }

    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        measureText()

        val mMeasuredWidth = measureWidth(widthMeasureSpec)
        val mMeasuredHeight = measureHeight(heightMeasureSpec)
        setMeasuredDimension(mMeasuredWidth, mMeasuredHeight)

        mTextStartX = (measuredWidth - mTextWidth) / 2
        mTextStartY = (measuredHeight - mTextHeight) / 2
    }

    /**
     * 绘图
     */
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (mDirection) {
            DIRECTION_LEFT -> {
                val mFocusStartX = mTextStartX
                val mFocusEndX = (mTextStartX + mTextProgress * mTextWidth).toInt()
                drawHorizontalText(canvas!!, mTextFocusColor, mFocusStartX, mFocusEndX)
                val mNormalStartX = (mTextStartX + mTextProgress * mTextWidth).toInt()
                val mNormalEndX = mTextStartX + mTextWidth
                drawHorizontalText(canvas, mTextNormalColor, mNormalStartX, mNormalEndX)
            }
            DIRECTION_RIGHT -> {
                val mFocusStartX = (mTextStartX + (1 - mTextProgress) * mTextWidth).toInt()
                val mFocusEndX = mTextStartX + mTextWidth
                drawHorizontalText(canvas!!, mTextFocusColor, mFocusStartX, mFocusEndX)
                val mNormalStartX = mTextStartX
                val mNormalEndX = (mTextStartX + (1 - mTextProgress) * mTextWidth).toInt()
                drawHorizontalText(canvas, mTextNormalColor, mNormalStartX, mNormalEndX)
            }
            DIRECTION_TOP -> {
                val mFocusStartY = mTextStartY
                val mFocusEndY = (mTextStartY + mTextProgress * mTextHeight).toInt()
                drawVerticalText(canvas!!, mTextFocusColor, mFocusStartY, mFocusEndY)
                val mNormalStartY = (mTextStartY + mTextProgress * mTextHeight).toInt()
                val mNormalEndY = mTextStartY + mTextHeight
                drawVerticalText(canvas, mTextFocusColor, mNormalStartY, mNormalEndY)
            }
            DIRECTION_BOTTOM -> {
                val mFocusStartY = (mTextStartY + (1 - mTextProgress) * mTextHeight).toInt()
                val mFocusEndY = mTextStartY + mTextHeight
                drawVerticalText(canvas!!, mTextFocusColor, mFocusStartY, mFocusEndY)
                val mNormalStartY = mTextStartY
                val mNormalEndY = (mTextStartY + (1 - mTextProgress) * mTextHeight).toInt()
                drawVerticalText(canvas, mTextFocusColor, mNormalStartY, mNormalEndY)
            }
        }
    }

    private fun drawHorizontalText(canvas: Canvas, mColor: Int, mStartX: Int, mEndX: Int) {
        mPaint.color = mColor
        canvas.save()
        canvas.clipRect(mStartX, 0, mEndX, measuredHeight)
        val x = mTextStartX.toFloat()
        val y = measuredHeight / 2F - ((mPaint.descent() + mPaint.ascent()) / 2F)
        canvas.drawText(mText!!, x, y, mPaint)
        canvas.restore()
    }

    private fun drawVerticalText(canvas: Canvas, mColor: Int, mStartY: Int, mEndY: Int) {
        mPaint.color = mColor
        canvas.save()
        canvas.clipRect(0, mStartY, measuredWidth, mEndY)
        val x = mTextStartX.toFloat()
        val y = measuredHeight / 2F - ((mPaint.descent() + mPaint.ascent()) / 2F)
        canvas.drawText(mText!!, x, y, mPaint)
        canvas.restore()
    }

    /**
     * 测量文本的宽高
     */
    private fun measureText() {
        mTextWidth = mPaint.measureText(mText).toInt()
        val mFontMetrics: Paint.FontMetrics = mPaint.fontMetrics
        mTextHeight = Math.ceil((mFontMetrics.descent - mFontMetrics.top).toDouble()).toInt()
        mPaint.getTextBounds(mText, 0, mText!!.length, mTextBound)
        mTextHeight = mTextBound.height()
    }

    private fun measureWidth(mMeasureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(mMeasureSpec)
        val size = View.MeasureSpec.getSize(mMeasureSpec)
        var result = 0
        when (mode) {
            View.MeasureSpec.EXACTLY -> result = size
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> {
                result = mTextWidth
                result += paddingLeft + paddingRight
            }
        }
        result = if (mode == View.MeasureSpec.AT_MOST) Math.min(result, size) else result
        return result
    }

    private fun measureHeight(mMeasureSpec: Int): Int {
        val mode = View.MeasureSpec.getMode(mMeasureSpec)
        val size = View.MeasureSpec.getSize(mMeasureSpec)
        var result = 0
        when (mode) {
            View.MeasureSpec.EXACTLY -> result = size
            View.MeasureSpec.AT_MOST, View.MeasureSpec.UNSPECIFIED -> {
                result = mTextBound.height()
                result += paddingTop + paddingBottom
            }
        }
        result = if (mode == View.MeasureSpec.AT_MOST) Math.min(result, size) else result
        return result
    }

    fun setText(mText: String) {
        this.mText = mText
        requestLayout()
        invalidate()
    }

    fun setTextSize(mTextSize: Float) {
        this.mTextSize = dp2px(mTextSize)
        requestLayout()
        invalidate()
    }

    fun setTextProgress(mTextProgress: Float) {
        this.mTextProgress = mTextProgress
        invalidate()
    }

    fun setTextFocusColor(mTextFocusColor: Int) {
        this.mTextFocusColor = mTextFocusColor
        invalidate()
    }

    fun setTextNormalColor(mTextNormalColor: Int) {
        this.mTextNormalColor = mTextNormalColor
        invalidate()
    }


    private val progress: String = "progress"
    private val state: String = "state"
    override fun onSaveInstanceState(): Parcelable? {
        val data = Bundle()
        data.putFloat(progress, mTextProgress)
        data.putParcelable(state, super.onSaveInstanceState())
        return data
    }

    override fun onRestoreInstanceState(data: Parcelable?) {
        if (data is Bundle) {
            mTextProgress = data.getFloat(progress)
            super.onRestoreInstanceState(data.getParcelable(state))
            return
        }
        super.onRestoreInstanceState(data)
    }

    private fun dp2px(dp: Float): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5F).toInt()
    }

    private fun sp2px(sp:Float):Int{
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,resources.displayMetrics).toInt()
    }
}

