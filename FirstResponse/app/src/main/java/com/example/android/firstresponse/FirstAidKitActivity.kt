package com.example.android.firstresponse

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.AlertDialog
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class FirstAidKitActivity : BaseActivity() {

    private lateinit var fab: FloatingActionButton
    private var dX = 0f
    private var dY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_aid_kit)

        // Load animations
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Initialize custom toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)

        // Set the title for the Toolbar
        supportActionBar?.title = getString(R.string.first_aid_kits)
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

        // Initialize RecyclerView and Adapter
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) // Set horizontal orientation
        recyclerView.adapter = FirstAidKitAdapter(getFirstAidKits())

        // Set up the Floating Action Button
        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener {
            showLocationDialog()
        }
        // Threshold for determining a drag
        val dragThreshold = 20 // Pixels

        fab.setOnTouchListener { view, motionEvent ->
            var isDragging = false // Flag to track if it's a drag
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - motionEvent.rawX
                    dY = view.y - motionEvent.rawY
                    isDragging = false // Reset drag flag on down
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = motionEvent.rawX + dX
                    val newY = motionEvent.rawY + dY

                    val deltaX = newX - view.x
                    val deltaY = newY - view.y

                    // If movement exceeds threshold, consider it a drag
                    if (Math.abs(deltaX) > dragThreshold || Math.abs(deltaY) > dragThreshold) {
                        view.animate()
                            .x(newX)
                            .y(newY)
                            .setDuration(0)
                            .start()
                        isDragging = true // It's a drag
                        true
                    } else {
                        false
                    }
                }
                MotionEvent.ACTION_UP -> {
                    // Perform click only if no drag occurred
                    if (!isDragging) {
                        view.performClick()
                    }
                    isDragging = false // Reset flag on up
                    false
                }
                else -> false
            }
        }
        // Show Snackbar message
        showSuggestionSnackbar(fab)
    }
    private fun showSuggestionSnackbar(view: View) {
        val snackbar = Snackbar.make(view, "Click the icon for suggestions", Snackbar.LENGTH_LONG)
        val snackbarView = snackbar.view

        // Set the background color to shadow2
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.context, R.color.shadow2))

        // Set the text color to white
        val snackbarTextView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarTextView.setTextColor(ContextCompat.getColor(view.context, android.R.color.white))

        snackbar.setAction("OK") {
            // Optional: Add action for Snackbar if needed
        }
        snackbar.show()
    }



    private fun showLocationDialog() {
        val options = arrayOf("Eyes", "Wounds", "Burns", "Cuts", "Pain Relief", "Emergency")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Where is the kit needed?")
        builder.setItems(options) { dialog, which ->
            val selectedLocation = options[which]
            filterFirstAidKits(selectedLocation)
        }
        builder.show()
    }

    private fun filterFirstAidKits(location: String) {
        // Logic to filter kits based on the selected location
        val filteredKits = getFirstAidKits().filter { kit ->
            when (location) {
                "Eyes" -> kit.title.contains("Eye", ignoreCase = true)
                "Wounds" -> kit.title.contains("Bandage", ignoreCase = true) ||
                        kit.title.contains("Gauze", ignoreCase = true) ||
                        kit.title.contains("Antiseptic", ignoreCase = true) ||
                        kit.title.contains("Alcohol Wipes", ignoreCase = true) ||
                        kit.title.contains("Cotton Balls", ignoreCase = true) ||
                        kit.title.contains("Tweezers", ignoreCase = true)
                "Burns" -> kit.title.contains("Burn Gel", ignoreCase = true) ||
                        kit.title.contains("Cold Compress", ignoreCase = true)
                "Cuts" -> kit.title.contains("Scissors", ignoreCase = true) ||
                        kit.title.contains("Adhesive Tape", ignoreCase = true)
                "Pain Relief" -> kit.title.contains("Pain Reliever", ignoreCase = true) ||
                        kit.title.contains("Hand Sanitizer", ignoreCase = true) ||
                        kit.title.contains("Gloves", ignoreCase = true)
                "Emergency" -> kit.title.contains("First Aid Manual", ignoreCase = true) ||
                        kit.title.contains("Emergency Blanket", ignoreCase = true) ||
                        kit.title.contains("Mask", ignoreCase = true) ||
                        kit.title.contains("Safety Pins", ignoreCase = true) ||
                        kit.title.contains("Splint", ignoreCase = true) ||
                        kit.title.contains("Finger Splint", ignoreCase = true) ||
                        kit.title.contains("Thermometer", ignoreCase = true) ||
                        kit.title.contains("Trauma Pad", ignoreCase = true)
                else -> true // Include all for "Others"
            }
        }

        // Update RecyclerView with filtered kits
        updateRecyclerView(filteredKits)
    }

    private fun updateRecyclerView(filteredKits: List<FirstAidKit>) {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.adapter = FirstAidKitAdapter(filteredKits)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the back button press to go back to the parent activity
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getFirstAidKits(): List<FirstAidKit> {
        return listOf(
            FirstAidKit(
                R.drawable.bandage, "Bandage", R.string.bandage_description,
                "https://shopee.ph/search?keyword=Bandage",
                "https://www.lazada.com.ph/catalog/?q=Bandage",
                "https://www.temu.com/search?q=Bandage"
            ),
            FirstAidKit(
                R.drawable.antiseptic, "Antiseptic", R.string.antiseptic_description,
                "https://shopee.ph/search?keyword=Antiseptic",
                "https://www.lazada.com.ph/catalog/?q=Antiseptic",
                "https://www.temu.com/search?q=Antiseptic"
            ),
            FirstAidKit(
                R.drawable.gauze, "Gauze", R.string.gauze_description,
                "https://shopee.ph/search?keyword=Gauze",
                "https://www.lazada.com.ph/catalog/?q=Gauze",
                "https://www.temu.com/search?q=Gauze"
            ),
            FirstAidKit(
                R.drawable.adhesive_tape, "Adhesive Tape", R.string.adhesive_tape_description,
                "https://shopee.ph/search?keyword=Adhesive%20Tape",
                "https://www.lazada.com.ph/catalog/?q=Adhesive+Tape",
                "https://www.temu.com/search?q=Adhesive+Tape"
            ),
            FirstAidKit(
                R.drawable.scissors, "Scissors", R.string.scissors_description,
                "https://shopee.ph/search?keyword=Scissors",
                "https://www.lazada.com.ph/catalog/?q=Scissors",
                "https://www.temu.com/search?q=Scissors"
            ),
            FirstAidKit(
                R.drawable.pain_reliever, "Pain Reliever", R.string.pain_reliever_description,
                "https://shopee.ph/search?keyword=Pain+Reliever",
                "https://www.lazada.com.ph/catalog/?q=Pain+Reliever",
                "https://www.temu.com/search?q=Pain+Reliever"
            ),
            FirstAidKit(
                R.drawable.thermometer, "Thermometer", R.string.thermometer_description,
                "https://shopee.ph/search?keyword=Thermometer",
                "https://www.lazada.com.ph/catalog/?q=Thermometer",
                "https://www.temu.com/search?q=Thermometer"
            ),
            FirstAidKit(
                R.drawable.alcohol_wipes, "Alcohol Wipes", R.string.alcohol_wipes_description,
                "https://shopee.ph/search?keyword=Alcohol+Wipes",
                "https://www.lazada.com.ph/catalog/?q=Alcohol+Wipes",
                "https://www.temu.com/search?q=Alcohol+Wipes"
            ),
            FirstAidKit(
                R.drawable.gloves, "Gloves", R.string.gloves_description,
                "https://shopee.ph/search?keyword=Gloves",
                "https://www.lazada.com.ph/catalog/?q=Gloves",
                "https://www.temu.com/search?q=Gloves"
            ),
            FirstAidKit(
                R.drawable.first_aid_manual, "First Aid Manual", R.string.first_aid_manual_description,
                "https://shopee.ph/search?keyword=First+Aid+Manual",
                "https://www.lazada.com.ph/catalog/?q=First+Aid+Manual",
                "https://www.temu.com/search?q=First+Aid+Manual"
            ),
            FirstAidKit(
                R.drawable.cold_compress, "Cold Compress", R.string.cold_compress_description,
                "https://shopee.ph/search?keyword=Cold+Compress",
                "https://www.lazada.com.ph/catalog/?q=Cold+Compress",
                "https://www.temu.com/search?q=Cold+Compress"
            ),
            FirstAidKit(
                R.drawable.tweezers, "Tweezers", R.string.tweezers_description,
                "https://shopee.ph/search?keyword=Tweezers",
                "https://www.lazada.com.ph/catalog/?q=Tweezers",
                "https://www.temu.com/search?q=Tweezers"
            ),
            FirstAidKit(
                R.drawable.safety_pins, "Safety Pins", R.string.safety_pins_description,
                "https://shopee.ph/search?keyword=Safety+Pins",
                "https://www.lazada.com.ph/catalog/?q=Safety+Pins",
                "https://www.temu.com/search?q=Safety+Pins"
            ),
            FirstAidKit(
                R.drawable.emergency_blanket, "Emergency Blanket", R.string.emergency_blanket_description,
                "https://shopee.ph/search?keyword=Emergency+Blanket",
                "https://www.lazada.com.ph/catalog/?q=Emergency+Blanket",
                "https://www.temu.com/search?q=Emergency+Blanket"
            ),
            FirstAidKit(
                R.drawable.mask, "Mask", R.string.mask_description,
                "https://shopee.ph/search?keyword=Mask",
                "https://www.lazada.com.ph/catalog/?q=Mask",
                "https://www.temu.com/search?q=Mask"
            ),
            FirstAidKit(
                R.drawable.splint, "Splint", R.string.splint_description,
                "https://shopee.ph/search?keyword=Splint",
                "https://www.lazada.com.ph/catalog/?q=Splint",
                "https://www.temu.com/search?q=Splint"
            ),
            FirstAidKit(
                R.drawable.eye_wash, "Eye Wash", R.string.eye_wash_description,
                "https://shopee.ph/search?keyword=Eye+Wash",
                "https://www.lazada.com.ph/catalog/?q=Eye+Wash",
                "https://www.temu.com/search?q=Eye+Wash"
            ),
            FirstAidKit(
                R.drawable.hand_sanitizer, "Hand Sanitizer", R.string.hand_sanitizer_description,
                "https://shopee.ph/search?keyword=Hand+Sanitizer",
                "https://www.lazada.com.ph/catalog/?q=Hand+Sanitizer",
                "https://www.temu.com/search?q=Hand+Sanitizer"
            ),
            FirstAidKit(
                R.drawable.cotton_balls, "Cotton Balls", R.string.cotton_balls_description,
                "https://shopee.ph/search?keyword=Cotton+Balls",
                "https://www.lazada.com.ph/catalog/?q=Cotton+Balls",
                "https://www.temu.com/search?q=Cotton+Balls"
            ),
            FirstAidKit(
                R.drawable.burn_gel, "Burn Gel", R.string.burn_gel_description,
                "https://shopee.ph/search?keyword=Burn+Gel",
                "https://www.lazada.com.ph/catalog/?q=Burn+Gel",
                "https://www.temu.com/search?q=Burn+Gel"
            ),
            FirstAidKit(
                R.drawable.trauma_pad, "Trauma Pad", R.string.trauma_pad_description,
                "https://shopee.ph/search?keyword=Trauma+Pad",
                "https://www.lazada.com.ph/catalog/?q=Trauma+Pad",
                "https://www.temu.com/search?q=Trauma+Pad"
            ),
            FirstAidKit(
                R.drawable.finger_splint, "Finger Splint", R.string.finger_splint_description,
                "https://shopee.ph/search?keyword=Finger+Splint",
                "https://www.lazada.com.ph/catalog/?q=Finger+Splint",
                "https://www.temu.com/search?q=Finger+Splint"
            )
        )
    }
}
