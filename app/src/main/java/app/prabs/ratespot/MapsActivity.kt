package app.prabs.ratespot

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import app.prabs.ratespot.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var currentLatLng: LatLng
    private lateinit var binding: ActivityMapsBinding
    private lateinit var currentLocMarker: Marker

    private val REQUEST_LOCATION_PERMISSION = 1
    private var currentUser: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
    private val db = Firebase.firestore
    private val docRef = db.collection("user").document(currentUser.uid).collection("reviews")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil
            .setContentView(this, R.layout.activity_maps)
        binding.rateButton.setOnClickListener{navigateToRate()}
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragEnd(p0: Marker?) {
                if (p0 != null) {
                    currentLatLng = LatLng(p0.position.latitude,p0.position.longitude)
                    currentLocMarker.position = currentLatLng
                    animateAndMark()
                }
            }
            override fun onMarkerDragStart(p0: Marker?) {
                Log.d("Maker","onMarkerDragStart")
            }
            override fun onMarkerDrag(p0: Marker?) {
                Log.d("Maker","onMarkerDrag")
            }
        })
        plotReviewMarkers()
        enableLocation()
    }

    private fun plotReviewMarkers(){
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("TAG", "Listen failed.", e)
            }
            if (snapshot != null) {
                plotAllReviews(snapshot.documents)
            } else {
                Log.d("TAG", "Current data: null")
            }
        }
    }

    private fun plotAllReviews(review:List<DocumentSnapshot>){
        review.forEach{
            val latitude: Double = it.get("latitude").toString().toDouble()
            val longitude: Double = it.get("longitude").toString().toDouble()
            val address = it.get("address")
            val status = it.get("status").toString()

            mMap.addMarker(
                MarkerOptions()
                    .position(LatLng(latitude, longitude))
                    .title("$address -- $status")
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor(status)))
            )
        }

    }

    private fun markerColor(status:String): Float {
        return when(status){
            "TERRIBLE"->BitmapDescriptorFactory.HUE_RED
            "BAD"->BitmapDescriptorFactory.HUE_ORANGE
            "AVERAGE"->BitmapDescriptorFactory.HUE_YELLOW
            "GOOD"->BitmapDescriptorFactory.HUE_GREEN
            "EXCELLENT"->BitmapDescriptorFactory.HUE_AZURE
            else -> BitmapDescriptorFactory.HUE_BLUE

        }
    }

    private fun checkPermission() : Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableLocation() {
        if (checkPermission()) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    drawCurrentLocationMarker()
                }
            }
        }
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    private fun drawCurrentLocationMarker(){
        val height = 150
        val width = 150
        val bitMap: BitmapDrawable = resources.getDrawable(R.drawable.marker) as BitmapDrawable
        val marker: Bitmap = Bitmap.createScaledBitmap(bitMap.bitmap, width, height, false)

        currentLocMarker = mMap.addMarker(
            MarkerOptions()
                .position(currentLatLng)
                .icon(BitmapDescriptorFactory.fromBitmap(marker))
                .draggable(true)
                .zIndex(10f)
                .title("You")
        )
        animateAndMark()
    }

    private fun animateAndMark(){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 19f))
        binding.locationText.text = findAddress(currentLatLng.latitude,currentLatLng.longitude)
    }

    private fun navigateToRate(){
        val intent = Intent (this, RatingActivity::class.java)
        intent.putExtra("address",binding.locationText.text)
        intent.putExtra("latitude","${currentLatLng.latitude}")
        intent.putExtra("longitude","${currentLatLng.longitude}")
        startActivity(intent)
    }

    private fun findAddress(latitude:Double,longitude:Double):String{
        val addresses: List<Address>
        val geocode = Geocoder(this, Locale.getDefault())
        addresses = geocode.getFromLocation(latitude, longitude, 1)
        return addresses[0].getAddressLine(0)
    }

    override fun onMarkerClick(p0: Marker?) = false
}
