package com.polotechnologies.mobifish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class HostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_host)
    }
}
