package com.example.libbliy.coolweather.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import com.example.libbliy.coolweather.R
import com.example.libbliy.coolweather.util.replaceFragmentInActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: ChooseAreaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val taskId = intent.getStringExtra(ChooseAreaFragment.ARGUMENT_EDIT_TASK_ID)

        val human = Human(this)

        val chooseAreaFragment =
                fragmentManager.findFragmentById(R.id.contentFrame) as ChooseAreaFragment?
                        ?: ChooseAreaFragment.newInstance(taskId).also {
                            replaceFragmentInActivity(it, R.id.contentFrame)
                        }

        viewModel = ViewModelProviders.of(this, human.myViewModelProvider)[ChooseAreaViewModel::class.java].apply {
            flag.observe(this@MainActivity, Observer {
                if (it == 3) {
                    intentStart(weatherId)
                }
            })
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (preferences.getString("weather", null) != null) {
            val intent = Intent(this, WeatherActivity::class.java)
            startActivity(intent)
            finish()
        }

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {

                R.id.choose_area_navigation_menu_item -> {
                    //
                }
            }

            drawer_layout.closeDrawers()

            true
        }

//        val intent2 = Intent(this, WeatherActivity::class.java)
//        intent2.putExtra("weather_id","CN101040100")
//        startActivity(intent2)
//        finish()

//        val cityPresenter = CityPresenter(In.pr(this).getDao(), chooseAreaFragment)
//        JsonHlr.areaDao = In.pr(this).getDao()
    }

    fun intentStart(weatherId: String) {
        val intent = Intent(this, WeatherActivity::class.java)
        intent.putExtra("weather_id", weatherId)
        startActivity(intent)
        finish()
    }
}
