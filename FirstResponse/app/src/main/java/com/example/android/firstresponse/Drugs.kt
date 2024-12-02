package com.example.android.firstresponse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat

class Drugs : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drugs)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this, R.color.shadow2)))

        getSupportActionBar()?.setTitle("DRUGS");

        // calling the action bar
        var actionBar = getSupportActionBar()

        // showing the back button in action bar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        //switching to consumption activity
        val buttonconsumption=findViewById<Button>(R.id.buttonconsumption)
        buttonconsumption.setOnClickListener{
            val Intent= Intent(this,consumption::class.java)
            startActivity(Intent)
        }

        //switching to substances activity
        val buttonsubstances=findViewById<Button>(R.id.buttonsubstances)
        buttonsubstances.setOnClickListener{
            val Intent= Intent(this,substances::class.java)
            startActivity(Intent)
        }

        //switching to mythsandreality activity
        val buttonmyths=findViewById<Button>(R.id.buttonmyth)
        buttonmyths.setOnClickListener{
            val Intent= Intent(this,MythsAndReality::class.java)
            startActivity(Intent)
        }

        //switching to prevention activity
        val buttonprevention=findViewById<Button>(R.id.buttonpreventiondrugs)
        buttonprevention.setOnClickListener{
            val Intent= Intent(this,DrugsPrevention::class.java)
            startActivity(Intent)
        }




    }
}