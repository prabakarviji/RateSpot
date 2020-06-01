package app.prabs.ratespot

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private enum class Rating(val value: Int) {
    EMPTY(R.string.terrible),
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

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        rating = rating.next()
        Log.i("###",resources.getString(rating.value))
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
        canvas.drawRoundRect(0f, 0f, width.toFloat (), height.toFloat(),20f,20f,paint)
        paint.color = Color.GRAY
        paint.style = Paint.Style.STROKE
        canvas.drawRoundRect(0f, 0f, width.toFloat (), height.toFloat(),20f,20f,paint)
        for (i in Rating.values()) {
            pointPosition.computeXYForSpeed(i)
            if(i.ordinal != 0 && i.ordinal != 5) {
                paint.color = Color.GRAY
                canvas.drawLine(pointPosition.x, 0f, pointPosition.x, pointPosition.y, paint)

            }
        }

    }
}
