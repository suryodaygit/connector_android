package com.cmrk.util.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.appcompat.widget.AppCompatImageView
import android.view.animation.Animation


class CircleImageView(context: Context, color: Int) : AppCompatImageView(context) {
    private var mListener: Animation.AnimationListener? = null
    internal var mShadowRadius: Int = 0

    init {
        val density = this.context.resources.displayMetrics.density
        val shadowYOffset = (density * 1.75f).toInt()
        val shadowXOffset = (density * 0.0f).toInt()
        this.mShadowRadius = (density * 3.5f).toInt()
        val circle: ShapeDrawable
        if (this.elevationSupported()) {
            circle = ShapeDrawable(OvalShape())
            ViewCompat.setElevation(this, 4.0f * density)
        } else {
            val oval = OvalShadow(this.mShadowRadius)
            circle = ShapeDrawable(oval)
            this.setLayerType(1, circle.paint)
            circle.paint.setShadowLayer(
                this.mShadowRadius.toFloat(),
                shadowXOffset.toFloat(),
                shadowYOffset.toFloat(),
                503316480
            )
            val padding = this.mShadowRadius
            this.setPadding(padding, padding, padding, padding)
        }

        circle.paint.color = color
        ViewCompat.setBackground(this, circle)
    }

    private fun elevationSupported(): Boolean {
        return Build.VERSION.SDK_INT >= 21
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!this.elevationSupported()) {
            this.setMeasuredDimension(
                this.measuredWidth + this.mShadowRadius * 2,
                this.measuredHeight + this.mShadowRadius * 2
            )
        }

    }

    fun setAnimationListener(listener: Animation.AnimationListener) {
        this.mListener = listener
    }

    public override fun onAnimationStart() {
        super.onAnimationStart()
        if (this.mListener != null) {
            this.mListener!!.onAnimationStart(this.animation)
        }

    }

    public override fun onAnimationEnd() {
        super.onAnimationEnd()
        if (this.mListener != null) {
            this.mListener!!.onAnimationEnd(this.animation)
        }

    }

    fun setBackgroundColorRes(colorRes: Int) {
        this.setBackgroundColor(ContextCompat.getColor(this.context, colorRes))
    }

    override fun setBackgroundColor(color: Int) {
        if (this.background is ShapeDrawable) {
            (this.background as ShapeDrawable).paint.color = color
        }

    }

    private inner class OvalShadow internal constructor(shadowRadius: Int) : OvalShape() {
        private var mRadialGradient: RadialGradient? = null
        private val mShadowPaint = Paint()

        init {
            mShadowRadius = shadowRadius
            this.updateRadialGradient(this.rect().width().toInt())
        }

        override fun onResize(width: Float, height: Float) {
            super.onResize(width, height)
            this.updateRadialGradient(width.toInt())
        }

        override fun draw(canvas: Canvas, paint: Paint) {
            val viewWidth = this@CircleImageView.width
            val viewHeight = this@CircleImageView.height
            canvas.drawCircle(
                (viewWidth / 2).toFloat(),
                (viewHeight / 2).toFloat(),
                (viewWidth / 2).toFloat(),
                this.mShadowPaint
            )
            canvas.drawCircle(
                (viewWidth / 2).toFloat(),
                (viewHeight / 2).toFloat(),
                (viewWidth / 2 - mShadowRadius).toFloat(),
                paint
            )
        }

        private fun updateRadialGradient(diameter: Int) {
            this.mRadialGradient = RadialGradient(
                (diameter / 2).toFloat(),
                (diameter / 2).toFloat(),
                mShadowRadius.toFloat(),
                intArrayOf(1023410176, 0),
                null,
                Shader.TileMode.CLAMP
            )
            this.mShadowPaint.shader = this.mRadialGradient
        }
    }

    companion object {
        private val KEY_SHADOW_COLOR = 503316480
        private val FILL_SHADOW_COLOR = 1023410176
        private val X_OFFSET = 0.0f
        private val Y_OFFSET = 1.75f
        private val SHADOW_RADIUS = 3.5f
        private val SHADOW_ELEVATION = 4
    }
}