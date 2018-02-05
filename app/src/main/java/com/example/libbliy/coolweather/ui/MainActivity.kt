package com.example.libbliy.coolweather.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.libbliy.coolweather.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val intent = Intent(this, WeatherActivity::class.java)
        if (preferences.getString("weather",null)!=null) {
            startActivity(intent)
            finish()
        }
    }

}
