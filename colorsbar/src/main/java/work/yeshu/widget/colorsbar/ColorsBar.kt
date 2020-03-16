/*
 *
 *  * Copyright (c) 2018 yeshu. All Rights Reserved.
 *
 */

package work.yeshu.widget.colorsbar

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View


class ColorsBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var mBarHeight: Int = 0
    private var mBarDividerWidth: Int = 0
    private var mBarDividerColor: Int = 0
    private var mLabelTextSize: Float = 0F
    private var mLabelTextColor: Int = 0
    private var mLabelTextMargin: Int = 0

    private var mData: ColorsBarData = ColorsBarDataUtil.getStressBarData(context)

    private val mPaint: Paint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private val mPath: Path by lazy { Path() }
    private val mRectF: RectF by lazy { RectF() }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorsBar)
        mBarHeight = typedArray.getDimensionPixelSize(
            R.styleable.ColorsBar_colors_bar_height, 10
        )
        mBarDividerWidth = typedArray.getDimensionPixelSize(
            R.styleable.ColorsBar_colors_bar_divider_width, 3
        )
        mBarDividerColor = typedArray.getColor(
            R.styleable.ColorsBar_colors_bar_divider_color, Color.WHITE
        )
        mLabelTextSize = typedArray.getDimension(
            R.styleable.ColorsBar_colors_bar_text_size, 14f
        )
        mLabelTextColor = typedArray.getColor(
            R.styleable.ColorsBar_colors_bar_text_color, Color.BLACK
        )
        mLabelTextMargin = typedArray.getDimensionPixelSize(
            R.styleable.ColorsBar_colors_bar_text_margin, 10
        )
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mPaint.textSize = mLabelTextSize
        val labelTextHeight = mPaint.fontMetrics.bottom - mPaint.fontMetrics.top
        val desireHeight =
            paddingTop + paddingBottom + mLabelTextMargin + mBarHeight + labelTextHeight
        setMeasuredDimension(widthMeasureSpec, resolveSize(desireHeight.toInt(), heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            val radius = mBarHeight / 2f
            val barWidth = width - paddingStart - paddingEnd

            // draw bar
            canvas.save()
            var startX = paddingStart
            val startY = paddingTop.toFloat()
            val endY = paddingTop + mBarHeight.toFloat()
            var endX: Float

            mRectF.set(paddingStart.toFloat(), startY, (width - paddingEnd).toFloat(), endY)
            mPath.addRoundRect(
                mRectF, radius, radius, Path.Direction.CW
            )
            canvas.clipPath(mPath)

            for (i in mData.rangeColors.indices) {
                endX = if (i == mData.rangeColors.size - 1) {
                    // 怕计算的时候因为四舍五入原因，导致最后的endX不正确，所以直接判断等于
                    (width - paddingEnd).toFloat()
                } else {
                    val sectionValue = if (i == 0) {
                        // 第0个位置的值为1,不能直接减
                        mData.rangeValues[i + 1]
                    } else {
                        mData.rangeValues[i + 1] - mData.rangeValues[i]
                    }
                    startX + barWidth * sectionValue * 1f / 100
                }
                mPaint.style = Paint.Style.FILL
                mPaint.color = mData.rangeColors[i]
                canvas.drawRect(startX.toFloat(), startY, endX, endY, mPaint)
                startX = endX.toInt()

                if (i < mData.rangeColors.size - 1) {
                    mPaint.color = mBarDividerColor
                    mPaint.strokeWidth = mBarDividerWidth.toFloat()
                    mPaint.style = Paint.Style.STROKE
                    canvas.drawLine(startX.toFloat(), startY, startX.toFloat(), endY, mPaint)
                }
            }
            canvas.restore()

            // draw label text
            mPaint.textSize = mLabelTextSize
            mPaint.color = mLabelTextColor
            for (i in mData.rangeValues.indices) {
                when (i) {
                    0 -> {
                        drawStartValueText(canvas, mData.rangeValues[i].toString())
                    }
                    mData.rangeValues.size - 1 -> {
                        drawEndValueText(canvas, mData.rangeValues[i].toString())
                    }
                    else -> {
                        val rangeWidth =
                            barWidth * ((mData.rangeValues[i]) * 1f) / 100
                        drawLabelText(canvas, mData.rangeValues[i].toString(), rangeWidth)
                    }
                }
            }
        }
    }

    private fun drawLabelText(canvas: Canvas, text: String, labelTextCenterX: Float) {
        val y =
            paddingTop + mBarHeight + mLabelTextMargin + (mPaint.fontMetrics.bottom - mPaint.fontMetrics.top)
        val textWidth = mPaint.measureText(text)
        val x = labelTextCenterX - textWidth / 2
        canvas.drawText(text, x, y, mPaint)
    }


    private fun drawStartValueText(canvas: Canvas, text: String) {
        val y =
            paddingTop + mBarHeight + mLabelTextMargin + (mPaint.fontMetrics.bottom - mPaint.fontMetrics.top)
        canvas.drawText(text, 0f, y, mPaint)
    }

    private fun drawEndValueText(
        canvas: Canvas, text: String
    ) {
        val y =
            paddingTop + mBarHeight + mLabelTextMargin + (mPaint.fontMetrics.bottom - mPaint.fontMetrics.top)
        val textWidth = mPaint.measureText(text)
        canvas.drawText(text, (width - paddingEnd - textWidth), y, mPaint)
    }

    fun updateData(value: List<Int>) {
        mData.rangeValues = value
        invalidate()
    }
}