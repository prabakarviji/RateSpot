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
    TERRIBLE(R.string.terrible),
    BAD(R.string.bad),
    AVERAGE(R.string.average),
    GOOD(R.string.good),
    EXCELLENT(R.string.excellent);
}

private const val RADIUS_OFFSET_LABEL = 30
private const val RADIUS_OFFSET_INDICATOR = -35

class RatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
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
        Log.i("####", pos.ordinal.toString())
        var rad = width/5
        x = (pos.ordinal * rad).toFloat()
        y = height.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = if (fanSpeed == Rating.TERRIBLE) Color.GRAY else Color.GREEN
        canvas.drawRoundRect(0f, 0f, width.toFloat (), height.toFloat(), 30F,30F,paint)
        paint.color = Color.BLACK
        for (i in Rating.values()) {
            pointPosition.computeXYForSpeed(i)
            if(i.ordinal != 0) canvas.drawLine(pointPosition.x, 0f, pointPosition.x, pointPosition.y, paint)
        }

    }
}
