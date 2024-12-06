package com.example.android.firstresponse

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import android.graphics.drawable.ColorDrawable
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationSettingsResponse
import android.content.ActivityNotFoundException
import android.view.Menu
import com.google.android.gms.location.Priority

class MainActivity : BaseActivity() {
    private val items = listOf(
        "burns",
        "choking",
        "cpr",
        "splints",
        "seizures",
        "shock",
        "bleeding",
        "snakebite",
        "bruises",
        "sprains",
        "strains",
        "nosebleeds",
        "allergic reaction",
        "headaches",
        "minor concussions",
        "muscle cramp",
        "blister",
        "anxiety management",
        "panic attack response",
        "trauma-informed care",
        "grounding techniques",
        "stress reduction",
        "floods",
        "acute grief",
        "volcanic eruption",
        "epidemic",
        "earthquake",
        // New additions
        "water safety",
        "road safety",
        "daily food safety",
        "emergency food safety",
        "heatwave"
    )


    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var buttonQuiz: Button
    private lateinit var buttonFirstAidKits: Button
    private lateinit var buttonSafetyPreparedness: Button
    private lateinit var buttonFlashlight: Button

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val LOCATION_SETTINGS_REQUEST_CODE = 2
        private const val EMAIL_PERMISSION_REQUEST_CODE = 3
        private const val TAG = "MainActivity"
    }

    private var isLocationRequestInProgress = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check if the user is logged in
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // If the user is logged in, navigate to UserInfoActivity
            startActivity(Intent(this, UserInfoActivity::class.java))
            finish()  // Close MainActivity so the user can't return to it by pressing back
            return  // Stop further execution of onCreate
        }

        // Existing setup for action bar, drawer, and buttons
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))

        drawerLayout = findViewById(R.id.drawer_Layout)
        actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Initialize buttons and their click listeners
        findViewById<Button>(R.id.button1).setOnClickListener {
            startActivity(Intent(this, FirstAid::class.java))
        }
        findViewById<Button>(R.id.button2).setOnClickListener {
            startActivity(Intent(this, PsycFA::class.java))
        }
        findViewById<Button>(R.id.button3).setOnClickListener {
            startActivity(Intent(this, DisasterManagement::class.java))
        }
        findViewById<Button>(R.id.button4).setOnClickListener {
            startActivity(Intent(this, BasicSanitation::class.java))
        }

        // Additional buttons for health facilities
        val buttonPolanguiRhu: Button = findViewById(R.id.buttonPolanguiRhu)
        val buttonOasRhu: Button = findViewById(R.id.buttonOasRhu)
        val buttonLigaoCityHospital: Button = findViewById(R.id.buttonZone)
        val buttonLigaoRhu: Button = findViewById(R.id.buttonLigaoRhu)
        val buttonDuran: Button = findViewById(R.id.buttonDuran)

        buttonPolanguiRhu.setOnClickListener {
            val intent = Intent(this, PolanguiRhuActivity::class.java)
            startActivity(intent)
        }

        buttonOasRhu.setOnClickListener {
            val intent = Intent(this, OasRhuActivity::class.java)
            startActivity(intent)
        }

        buttonLigaoCityHospital.setOnClickListener {
            val intent = Intent(this, ZoneActivity::class.java)
            startActivity(intent)
        }

        buttonLigaoRhu.setOnClickListener {
            val intent = Intent(this, LigaoRhuActivity::class.java)
            startActivity(intent)
        }

        buttonDuran.setOnClickListener {
            val intent = Intent(this, DuranHospitalActivity::class.java)
            startActivity(intent)
        }

        buttonQuiz = findViewById(R.id.buttonQuiz)
        buttonQuiz.setOnClickListener {
            startActivity(Intent(this, QuizPageActivity::class.java))
        }

        buttonFirstAidKits = findViewById(R.id.buttonFirstAidKits)
        buttonFirstAidKits.setOnClickListener {
            startActivity(Intent(this, FirstAidKitActivity::class.java))
        }

        buttonSafetyPreparedness = findViewById(R.id.buttonSafetyPreparedness)
        buttonSafetyPreparedness.setOnClickListener {
            startActivity(Intent(this, SafetyAndPreparedness::class.java))
        }

        buttonFlashlight = findViewById<Button>(R.id.buttonFlashlight)
        buttonFlashlight.setOnClickListener {
            startActivity(Intent(this, FlashlightActivity::class.java))
        }

        // Bottom navigation setup
        findViewById<BottomNavigationView>(R.id.bottomNavigationView).setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottomHelpline -> {
                    val intent = Intent(this, HelplineActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                else -> false
            }
            when (menuItem.itemId) {
                R.id.bottomSaved -> {
                    val intent = Intent(this, SavedActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    true
                }
                else -> false
            }
        }

        // Initialize navigation drawer and its item selected listener
        findViewById<NavigationView>(R.id.navigationView).setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.location -> {
                    if (!isLocationRequestInProgress) {
                        isLocationRequestInProgress = true
                        if (checkLocationPermission()) {
                            enableLocationSettings()
                        } else {
                            requestLocationPermission()
                        }
                    }
                    true
                }
                R.id.login -> {
                    startActivity(Intent(this, LoginActivity::class.java)) // Handle Settings
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java)) // Handle Settings
                    true
                }
                R.id.credits -> {
                    startActivity(Intent(this, CreditsActivity::class.java)) // Handle Credits
                    true
                }
                R.id.suggestions -> {
                    if (checkEmailAppPermission()) {
                        openEmailApp()
                    } else {
                        requestEmailAppPermission()
                    }
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView // Correct casting

        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    performSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally handle text changes here
                return true
            }
        })

        return true
    }



    override fun onResume() {
        super.onResume()
        isLocationRequestInProgress = false  // Reset the flag on resume

        // Set the home menu item as selected
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.menu.findItem(R.id.bottomHome).isChecked = true

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        navigationView.menu.findItem(R.id.sideHome).isChecked = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else if (!checkLocationPermission()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            showSettingsDialog()
        }
    }

    private fun enableLocationSettings() {
        // Create a location request with high accuracy using the Builder
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000) // Update interval of 1 second
            .setMinUpdateIntervalMillis(500) // Set minimum update interval to 0.5 seconds
            .build()

        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            Log.d(TAG, "Location settings are satisfied.")
            getCurrentLocationAndOpenGoogleMaps()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, LOCATION_SETTINGS_REQUEST_CODE)
                } catch (sendEx: Exception) {
                    Log.e(TAG, "Error resolving location settings: ${sendEx.localizedMessage}")
                }
            } else {
                Log.e(TAG, "Location settings are not adequate.")
                Toast.makeText(this, "Location settings are not adequate, and cannot be fixed here. Fix in Settings.", Toast.LENGTH_LONG).show()
            }
        }.addOnCompleteListener {
            isLocationRequestInProgress = false  // Reset the flag once task is complete
        }
    }


    private fun getCurrentLocationAndOpenGoogleMaps() {
        if (checkLocationPermission()) {
            // Updated location request using Builder
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000) // Every 5 seconds
                .setMinUpdateIntervalMillis(500) // Allow updates every 2 seconds
                .build()

            val locationCallback = object : com.google.android.gms.location.LocationCallback() {
                override fun onLocationResult(locationResult: com.google.android.gms.location.LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    if (location != null) {
                        val userLocation = Pair(location.latitude, location.longitude)
                        openGoogleMaps(userLocation)
                        fusedLocationClient.removeLocationUpdates(this) // Stop location updates after obtaining the location
                    } else {
                        Log.e(TAG, "Location is null.")
                        Toast.makeText(this@MainActivity, "Unable to retrieve current location. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            // Request location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
                .addOnSuccessListener {
                    // Optionally, immediately check for the last known location as a fallback
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            val userLocation = Pair(location.latitude, location.longitude)
                            openGoogleMaps(userLocation)
                        }
                    }
                }.addOnFailureListener {
                    Log.e(TAG, "Failed to get location updates: ${it.localizedMessage}")
                    Toast.makeText(this, "Failed to retrieve current location. Please try again.", Toast.LENGTH_SHORT).show()
                }
        } else {
            requestLocationPermission()
        }
    }



    private fun openGoogleMaps(userLocation: Pair<Double, Double>) {
        val locations = listOf(
            Pair(13.292603487659566, 123.49065310834811), // RHU Polangui
            Pair(13.257769416930843, 123.49944847998052), // RHU Oas
            Pair(13.239677184699186, 123.55644399532457), // Josefina Belmonte Duran Albay Provincial Hospital
            Pair(13.239371029764875, 123.53186333310623), // Zone
            Pair(13.240612986354503, 123.54160481199426)  // Ligao RHU
        )

        val nearestLocation = locations.minByOrNull { distance(userLocation, it) }

        val nearestLocationUri = if (nearestLocation != null) {
            Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${nearestLocation.first},${nearestLocation.second}")
        } else {
            Uri.parse("https://www.google.com/maps")
        }

        val mapIntent = Intent(Intent.ACTION_VIEW, nearestLocationUri)
        startActivity(mapIntent)
    }

    private fun distance(loc1: Pair<Double, Double>, loc2: Pair<Double, Double>): Double {
        val earthRadius = 6371.0 // Earth radius in kilometers

        val dLat = Math.toRadians(loc2.first - loc1.first)
        val dLng = Math.toRadians(loc2.second - loc1.second)
        val a = Math.pow(Math.sin(dLat / 2), 2.0) +
                Math.cos(Math.toRadians(loc1.first)) * Math.cos(Math.toRadians(loc2.first)) * Math.pow(Math.sin(dLng / 2), 2.0)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableLocationSettings()
                } else {
                    isLocationRequestInProgress = false
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        showSettingsDialog()
                    } else {
                        Log.e(TAG, "Location permission denied")
                        Toast.makeText(this, "Permission denied. Please grant location access to use this feature.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            EMAIL_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openEmailApp()
                } else {
                    Toast.makeText(this, "Permission denied. Please grant email access to use this feature.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Permission Required")
            setMessage("Location access is required for this feature. Please enable it in the app settings.")
            setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            setNegativeButton("Cancel", null)
        }.show()
    }

    private fun checkEmailAppPermission(): Boolean {
        // No specific permission is needed for email apps
        return true
    }

    private fun requestEmailAppPermission() {
        // As of now, no specific email permission is needed
        Toast.makeText(this, "Email app access required.", Toast.LENGTH_SHORT).show()
    }

    private fun openEmailApp() {
        // Define the package name for Gmail
        val gmailPackageName = "com.google.android.gm"

        // Create an intent to send an email
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // Use mailto: to directly open email clients
            putExtra(Intent.EXTRA_EMAIL, arrayOf("firstresponse@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Suggestion")
        }

        // Create an intent to specifically open Gmail
        val gmailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // This will be used to check if Gmail can handle the intent
            setPackage(gmailPackageName) // Set the Gmail package name
        }

        // Check if Gmail is installed
        val packageManager = packageManager
        val gmailApps = packageManager.queryIntentActivities(gmailIntent, PackageManager.MATCH_DEFAULT_ONLY)

        if (gmailApps.isNotEmpty()) {
            // If Gmail is installed, use the Gmail intent
            try {
                startActivity(gmailIntent)
            } catch (e: ActivityNotFoundException) {
                // Handle the exception if needed
                Toast.makeText(this, "Unable to open Gmail app.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Fallback to any available email client
            try {
                startActivity(emailIntent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "No email apps found.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun performSearch(query: String) {
        val results = items.filter { it.contains(query, ignoreCase = true) }

        if (results.isNotEmpty()) {
            val intent = Intent(this, SearchResultsActivity::class.java).apply {
                putExtra("searchQuery", query) // Correctly pass search query
                putExtra("searchResults", results.toTypedArray())
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, "No results found for: $query", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            getCurrentLocationAndOpenGoogleMaps()
        }
    }
}
