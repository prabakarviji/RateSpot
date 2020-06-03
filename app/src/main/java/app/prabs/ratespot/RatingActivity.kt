package app.prabs.ratespot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.prabs.ratespot.databinding.ActivityRatingBinding
import kotlin.math.roundToLong


class RatingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingBinding

    private  var roadValue = 0.0f
    private  var lightValue = 0.0f
    private  var sewageValue = 0.0f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rating)
        binding.roadRating.setRatingViewListener(object : RatingView.RatingViewListener {
            override fun setRating(value: Int) {
                roadValue = value.toFloat()
                setRatingValues()
            }
        })
        binding.lightRating.setRatingViewListener(object : RatingView.RatingViewListener {
            override fun setRating(value: Int) {
                lightValue = value.toFloat()
                setRatingValues()
            }
        })
        binding.sewageRating.setRatingViewListener(object : RatingView.RatingViewListener {
            override fun setRating(value: Int) {
                sewageValue = value.toFloat()
                setRatingValues()
            }
        })


    }

    fun setRatingValues(){
        binding.roadValue.text =  roadValue.toString()
        binding.lightValue.text = lightValue.toString()
        binding.sewageValue.text = sewageValue.toString()
        val overall = (roadValue + lightValue +sewageValue)/3.0
        binding.overall.text = "Overall rating: ${String.format("%.1f",overall)}"
    }


}