package com.example.android.firstresponse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat

class TreatmentAids : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_aids)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))
        //to change title of action bar of respective activity
        getSupportActionBar()?.setTitle("TREATMENT FOR AIDS");

        //to show back button on action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    //function to make back button work
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}