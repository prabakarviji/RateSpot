package app.prabs.ratespot

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.withStyledAttributes

private enum class Rating(val value: Int) {
    EMPTY(R.string.empty),
    TERRIBLE(R.string.terrible),
    BAD(R.string.bad),
    AVERAGE(R.string.average),
    GOOD(R.string.good),
    EXCELLENT(R.string.excellent);

    fun find(num:Int) = when (num) {
        0 -> TERRIBLE
        1 -> BAD
        2 -> AVERAGE
        3 -> GOOD
        4 -> EXCELLENT
        else -> TERRIBLE
    }

}

class RatingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),View.OnClickListener {

    interface RatingViewListener {
        fun setRating(value: Int)
    }
    private var listener: RatingViewListener? = null

    fun setRatingViewListener(listener: RatingViewListener) {
        this.listener = listener
    }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.RateView) {
            terribleColor = getColor(R.styleable.RateView_terribleColor, 0)
            badColor = getColor(R.styleable.RateView_badColor, 0)
            averageColor = getColor(R.styleable.RateView_averageColor, 0)
            goodColor = getColor(R.styleable.RateView_goodColor, 0)
            excellentColor = getColor(R.styleable.RateView_excellentColor, 0)
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {

        super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                val position = event.x/(width/5)
                rating = rating.find(position.toInt())
                performClick()
                listener?.setRating(position.toInt()+1)
                return true
            }
        }
        return false
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true
        invalidate()
        return true
    }

    private var rectHeight = 50
    private var rating = Rating.EMPTY
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private var terribleColor = 0
    private var badColor = 0
    private var averageColor = 0
    private var goodColor = 0
    private var excellentColor = 0

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
        val unitWidth = width/5
        x = (pos.ordinal * unitWidth).toFloat()
        y = height.toFloat()
    }

    private fun findColor(i: Rating): Int {
        var color = Color.TRANSPARENT
        if(rating > i) {
            color = when (rating) {
                Rating.TERRIBLE -> terribleColor
                Rating.BAD -> badColor
                Rating.AVERAGE -> averageColor
                Rating.GOOD -> goodColor
                Rating.EXCELLENT -> excellentColor
                else -> Color.TRANSPARENT
            }
        }
        return color
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
            paint.color = findColor(i)
            paint.style = Paint.Style.FILL
            canvas.drawRect(pointPosition.x, 0f, pointPosition.x+(width/5), pointPosition.y, paint)
            paint.color = Color.GRAY
            paint.style = Paint.Style.STROKE
            canvas.drawRect(pointPosition.x, 0f, pointPosition.x+(width/5), pointPosition.y, paint)
            paint.color = if (i == Rating.EMPTY || i == Rating.EXCELLENT) Color.TRANSPARENT else Color.GRAY
            canvas.drawLine(pointPosition.x, 0f, pointPosition.x, pointPosition.y, paint)
        }

    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }

}
