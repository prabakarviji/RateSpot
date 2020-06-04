package app.prabs.ratespot

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import app.prabs.ratespot.databinding.ActivityRatingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp


class RatingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRatingBinding
    private lateinit var currentUser: FirebaseUser

    private  var roadValue = 0.0f
    private  var lightValue = 0.0f
    private  var sewageValue = 0.0f
    private  var overall = 0.0

    val firestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = FirebaseAuth.getInstance().currentUser!!
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
        binding.rateButton.setOnClickListener{submitRatings()}

    }

    private fun submitRatings(){
        val rating = hashMapOf(
            "road" to roadValue,
            "light" to lightValue,
            "sewage" to sewageValue,
            "overall" to overall,
            "address" to intent.getStringExtra("address"),
            "latitude" to intent.getStringExtra("latitude"),
            "longitude" to  intent.getStringExtra("longitude")
        )
        Log.d("####",currentUser.uid)
        firestore.collection("user").
            document(currentUser.uid).
            collection("reviews")
            .add(rating)
            .addOnSuccessListener { documentReference ->
                val toast = Toast.makeText(applicationContext, "Submitted Successfully", Toast.LENGTH_SHORT)
                toast.show()
            }
            .addOnFailureListener { e ->
                val toast = Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT)
                toast.show()
            }
    }

    private fun find(num:Int) = when (num) {
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
        overall = (roadValue + lightValue +sewageValue)/3.0
        binding.overall.text = "Overall rating: ${find(overall.toInt())}  ${String.format("%.1f",overall)}"
    }


}


