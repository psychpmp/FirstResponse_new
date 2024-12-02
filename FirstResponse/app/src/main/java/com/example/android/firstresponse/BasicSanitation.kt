package com.example.android.firstresponse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.graphics.drawable.ColorDrawable
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat

class BasicSanitation : BaseActivity() {

    private lateinit var webView1: WebView
    private lateinit var webView2: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basic_sanitation)

        // Load animations
        val pressAnim = AnimationUtils.loadAnimation(this, R.anim.button_press)
        val releaseAnim = AnimationUtils.loadAnimation(this, R.anim.button_release)

        // Initialize custom toolbar
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)

        // Set the title for the Toolbar
        supportActionBar?.title = getString(R.string.basic_sanitation_t)
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




    //to show back button on action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //webview
        webView1 = findViewById(R.id.webView)
        webView1.settings.javaScriptEnabled = true
        webView1.webViewClient = WebViewClient()


        val videoId = "IisgnbMfKvI"
        val videoUrl = "https://www.youtube.com/embed/$videoId"

        webView1.loadData(
            "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl\" frameborder=\"0\" allowfullscreen></iframe>",
            "text/html",
            "utf-8"
        )

        webView2 = findViewById(R.id.webView2)
        webView2.settings.javaScriptEnabled = true
        webView2.webViewClient = WebViewClient()


        val videoId2 = "kpgxMA5St0g"
        val videoUrl2 = "https://www.youtube.com/embed/$videoId2"

        webView2.loadData(
            "<iframe width=\"100%\" height=\"100%\" src=\"$videoUrl2\" frameborder=\"0\" allowfullscreen></iframe>",
            "text/html",
            "utf-8"
        )


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

    override fun onBackPressed() {
        if (webView1.canGoBack()) {
            webView1.goBack()
        }
        if (webView2.canGoBack()) {
            webView2.goBack()
        }

        else {
            super.onBackPressed()
        }
    }


}