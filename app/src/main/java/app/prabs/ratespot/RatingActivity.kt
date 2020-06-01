package app.prabs.ratespot

import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.prabs.ratespot.databinding.ActivityRatingBinding


class RatingActivity : AppCompatActivity(), OnRatingBarChangeListener {

    private lateinit var binding: ActivityRatingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_rating)
    }

    override fun onRatingChanged(ratingBar: RatingBar?, rating: Float,fromUser: Boolean) {
        if (fromUser){
            val anim = ObjectAnimator.ofFloat(ratingBar, "rating",0f, rating)
            anim.duration = 300
            anim.start()
        }
    }


}