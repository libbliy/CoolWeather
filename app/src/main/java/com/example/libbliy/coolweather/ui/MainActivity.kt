package com.example.libbliy.coolweather.ui

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.example.libbliy.coolweather.In
import com.example.libbliy.coolweather.R
import com.example.libbliy.coolweather.util.JsonHlr
import com.example.libbliy.coolweather.util.replaceFragmentInActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val taskId = intent.getStringExtra(ChooseAreaFragment.ARGUMENT_EDIT_TASK_ID)

        val chooseAreaFragment =
                fragmentManager.findFragmentById(R.id.contentFrame) as ChooseAreaFragment?
                        ?: ChooseAreaFragment.newInstance(taskId).also {
                            replaceFragmentInActivity(it, R.id.contentFrame)
                        }

        val cityPresenter=CityPresenter(In.pr(this).getDao(),chooseAreaFragment)
        JsonHlr.dao = In.pr(this).getDao()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (preferences.getString("weather", null) != null) {
        val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
