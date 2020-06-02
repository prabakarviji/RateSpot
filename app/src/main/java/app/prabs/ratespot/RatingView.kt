package app.prabs.ratespot

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast


private enum class Rating(val value: Int) {
    EMPTY(R.string.empty),
    TERRIBLE(R.string.terrible),
    BAD(R.string.bad),
    AVERAGE(R.string.average),
    GOOD(R.string.good),
    EXCELLENT(R.string.excellent);

    fun next() = when (this) {
        EMPTY -> TERRIBLE
        TERRIBLE -> BAD
        BAD -> AVERAGE
        AVERAGE -> GOOD
        GOOD -> EXCELLENT
        EXCELLENT -> EMPTY
    }
}

class RatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    init {
        isClickable = true
    }

    private var rating = Rating.EMPTY

    override fun onTouchEvent(event: MotionEvent): Boolean {

        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                var position = event.x/(width/5)
                performClick()
                return true
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        rating = rating.next()
        //contentDescription = resources.getString(fanSpeed.label)

        invalidate()
        return true
    }

    private var rectHeight = 80
    private var fanSpeed = Rating.TERRIBLE
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        rectHeight = height
    }

    private fun PointF.computeXYForSpeed(pos: Rating) {
        var distance = width/5
        x = (pos.ordinal * distance).toFloat()
        y = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.WHITE
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat (), height.toFloat(),paint)
        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        canvas.drawRect(0f, 0f, width.toFloat (), height.toFloat(),paint)
        for (i in Rating.values()) {
            pointPosition.computeXYForSpeed(i)
            if(i.ordinal != 0 && i.ordinal != 5) {
                paint.color = Color.GRAY
                canvas.drawLine(pointPosition.x, 0f, pointPosition.x, pointPosition.y, paint)

            }
            else{
                paint.color = Color.RED
                paint.style = Paint.Style.FILL
                canvas.drawRect(0f, 0f, width/5.toFloat (), height.toFloat(),paint)
            }
        }

    }
}
