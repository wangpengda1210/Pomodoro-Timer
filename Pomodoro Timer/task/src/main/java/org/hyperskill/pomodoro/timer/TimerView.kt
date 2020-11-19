package org.hyperskill.pomodoro.timer

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.concurrent.TimeUnit


class TimerView : View {
    
    private val THICKNESS_SCALE = 0.03f
    
    private lateinit var mBitMap: Bitmap
    private lateinit var mCanvas: Canvas
    
    private var mCirclePaint: Paint
    private var mErasePaint: Paint
    private var mTextPaint: Paint
    
    private lateinit var mCircleInnerBounds: RectF
    private lateinit var mCircleOuterBounds: RectF
    
    private var mCircleSweepAngle = 0f
    private var mTimerAnimator: ValueAnimator? = null
    
    var timerText = "00:00"
    var circleColor = Color.RED
    private val textSize = 200f
    
    
    constructor(context: Context) : this(context, null)
    constructor(context: Context, set: AttributeSet?) : this(context, set, 0)
    constructor(context: Context, set: AttributeSet?, defStyleAttr: Int) : super(context, set, defStyleAttr) {
    
        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.color = circleColor
        
        mErasePaint = Paint()
        mErasePaint.isAntiAlias = true
        mErasePaint.color = Color.TRANSPARENT
        mErasePaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        
        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = textSize
    
    }
    
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        if (w != oldw || h != oldh) {
            mBitMap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            mBitMap.eraseColor(Color.TRANSPARENT)
            mCanvas = Canvas(mBitMap)
        }
        
        super.onSizeChanged(w, h, oldw, oldh)
        updateBounds()
    }
    
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
    
    private fun updateBounds() {
        val thickness = width * THICKNESS_SCALE;
        
        mCircleOuterBounds = RectF(0F, 0F, width.toFloat(), height.toFloat())
        mCircleInnerBounds = RectF(mCircleOuterBounds.left + thickness,
                mCircleOuterBounds.top + thickness,
                mCircleOuterBounds.right - thickness,
                mCircleOuterBounds.bottom - thickness)
        
        invalidate()
    }
    
    override fun onDraw(canvas: Canvas?) {
        mCanvas.drawColor(0, PorterDuff.Mode.CLEAR)
        
        mCirclePaint.color = circleColor
        mCanvas.drawArc(mCircleOuterBounds, 270f,
                mCircleSweepAngle, true, mCirclePaint)
        mCanvas.drawOval(mCircleInnerBounds, mErasePaint)
        
        mCanvas.drawText(timerText, width / 2f - timerText.length * textSize / 4f,
                height / 2f + textSize / 4f, mTextPaint)
    
        canvas?.drawBitmap(mBitMap, 0f, 0f, null)
    }
    
    fun start(secs: Int) {
        stop()
        mTimerAnimator = ValueAnimator.ofFloat(0f, 1f)
        mTimerAnimator!!.duration = TimeUnit.SECONDS.toMillis(secs.toLong())
        mTimerAnimator!!.interpolator = LinearInterpolator()
        mTimerAnimator!!.addUpdateListener { animation -> drawProgress(animation.animatedValue as Float) }
        mTimerAnimator!!.start()
    }
    
    fun stop() {
        if (mTimerAnimator != null) {
            if (mTimerAnimator!!.isRunning) mTimerAnimator!!.cancel()
            mTimerAnimator = null
            drawProgress(0f)
        }
    }
    
    private fun drawProgress(progress: Float) {
        mCircleSweepAngle = 360 * progress
        invalidate()
    }
    
    fun getText() = timerText
    fun getColor() = circleColor
    
}