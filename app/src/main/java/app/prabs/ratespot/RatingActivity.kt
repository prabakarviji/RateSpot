package app.prabs.ratespot

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.prabs.ratespot.databinding.ActivityRatingBinding



class RatingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingBinding

    private  var roadValue = 0.0f
    private  var lightValue = 0.0f
    private  var sewageValue = 0.0f



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rating)
        binding.address.text = intent.getStringExtra("address")
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

    fun find(num:Int) = when (num) {
        1 -> "TERRIBLE"
        2 -> "BAD"
        3 -> "AVERAGE"
        4 -> "GOOD"
        5 -> "EXCELLENT"
        else -> ""
    }

    @SuppressLint("SetTextI18n")
    fun setRatingValues(){
        binding.roadValue.text =  "${find(roadValue.toInt())}  ${roadValue}"
        binding.lightValue.text = "${find(lightValue.toInt())}  ${lightValue}"
        binding.sewageValue.text = "${find(sewageValue.toInt())}  ${sewageValue}"
        val overall = (roadValue + lightValue +sewageValue)/3.0
        binding.overall.text = "Overall rating: ${find(overall.toInt())}  ${String.format("%.1f",overall)}"
    }


}