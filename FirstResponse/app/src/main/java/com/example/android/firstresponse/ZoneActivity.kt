package com.example.android.firstresponse

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

class ZoneActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val LIGAO_CITY_HOSPITAL_LAT = 13.239329255032152
        private const val LIGAO_CITY_HOSPITAL_LNG = 123.53173458708221
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zone)

        // Load animations
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Initialize custom toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)

        // Set the title for the Toolbar
        supportActionBar?.title = "ZONE MEDICAL AND INTERVENTION HOSPITAL"  // Replace with your desired title
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.red))


        // Show back button on the Toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back)

        // Set up back button click listener with animation
        toolbar.setNavigationOnClickListener {
            it.startAnimation(pressAnim)
            it.postDelayed({
                it.startAnimation(releaseAnim)
                finish()
            }, pressAnim.duration)
        }

        val content: TextView = findViewById(R.id.content_ligao_city_hospital)
        val buttonGetDirections: Button = findViewById(R.id.button_get_directions_ligao_city_hospital)
        val distanceTextView: TextView = findViewById(R.id.distance_text_view_ligao_city_hospital)
        val buttonContact: Button = findViewById(R.id.button_contact_ligao_city_hospital)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Define LocationCallback to receive location updates
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                if (location != null) {
                    // Calculate distance
                    val distance = calculateDistance(location.latitude, location.longitude, LIGAO_CITY_HOSPITAL_LAT, LIGAO_CITY_HOSPITAL_LNG)
                    distanceTextView.text = "Distance from you: ${"%.2f".format(distance)} meters"

                    // Open Maps with directions
                    openMapsWithDirections(location)
                    // Stop location updates after getting the location
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                }
            }
        }

        buttonGetDirections.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                if (isLocationEnabled()) {
                    requestLocationUpdates()
                } else {
                    // Prompt user to enable location services
                    promptForLocationSettings()
                }
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            }
        }

        buttonContact.setOnClickListener {
            dialPhoneNumber("123-456-7890") // Replace with the actual phone number
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun promptForLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds
            fastestInterval = 5000 // 5 seconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun openMapsWithDirections(currentLocation: Location) {
        val origin = "${currentLocation.latitude},${currentLocation.longitude}"
        val destination = "$LIGAO_CITY_HOSPITAL_LAT,$LIGAO_CITY_HOSPITAL_LNG"
        val uri = "https://www.google.com/maps/dir/?api=1&origin=$origin&destination=$destination"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.setPackage("com.google.android.apps.maps")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun calculateDistance(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0]
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish() // Close the activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
