package app.prabs.ratespot

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.prabs.ratespot.databinding.ActivityRatingBinding


class RatingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rating)
        binding.roadRating.setRatingViewListener(object : RatingView.RatingViewListener {

            override fun setRating(value: Int) {
                binding.roadValue.text = value.toFloat().toString()
            }
        })

    }


}