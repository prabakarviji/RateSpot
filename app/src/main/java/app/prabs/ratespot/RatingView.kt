package app.prabs.ratespot

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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
    private var dim_height = 80
    private var fanSpeed = Rating.TERRIBLE
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create( "", Typeface.BOLD)
    }
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        //radius = (min(width, height) / 2.0 * 0.8).toFloat()
        dim_height = height
    }

    private fun PointF.computeXYForSpeed(pos: Rating, radius: Float) {
        // Angles are in radians.
        val startAngle = Math.PI * (9 / 8.0)
        val angle = startAngle + pos.ordinal * (Math.PI / 4)
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + height / 2
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = if (fanSpeed == Rating.TERRIBLE) Color.GREEN else Color.GREEN
        canvas.drawRoundRect(0f, 0f, width.toFloat (), height.toFloat(), 30F,30F,paint)
    }
}
