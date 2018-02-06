package com.example.libbliy.coolweather.ui

import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.annotation.ColorRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.example.libbliy.coolweather.R
import com.example.libbliy.coolweather.gson.Weather
import com.example.libbliy.coolweather.util.HttpUtil
import com.example.libbliy.coolweather.util.JsonHlr
import kotlinx.android.synthetic.main.activity_weather.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class WeatherActivity : AppCompatActivity() {


    private lateinit var weatherLayout: ScrollView
    private lateinit var titleCity: TextView
    private lateinit var titleUpdateTime: TextView
    private lateinit var degreeText: TextView
    private lateinit var weatherInfoText: TextView
    private lateinit var aqiText: TextView
    private lateinit var pm25Text: TextView
    private lateinit var comfortText: TextView
    private lateinit var carWashText: TextView
    private lateinit var sportText: TextView
    private lateinit var forecastLayout: LinearLayout
    private lateinit var bingPicImg: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21

        ) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            Log.w("decorView", decorView.systemUiVisibility.toString())
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)

        weatherLayout = findViewById(R.id.weather_layout)
        titleCity = findViewById(R.id.title_city)
        titleUpdateTime = findViewById(R.id.title_update_time)
        degreeText = findViewById(R.id.degree_text)
        weatherInfoText = findViewById(R.id.weather_info_text)
        aqiText = findViewById(R.id.aqi_text)
        pm25Text = findViewById(R.id.pm25_text)
        comfortText = findViewById(R.id.comfort_text)
        carWashText = findViewById(R.id.car_wash_text)
        sportText = findViewById(R.id.sport_text)
        forecastLayout = findViewById(R.id.forecast_layout)
        bingPicImg = findViewById(R.id.bing_pic_img)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = preferences.getString("weather", null)
        if (weatherString != null) {
            //有缓存直接解析天气数据
            val weather = JsonHlr.halResponseWeather(weatherString)
            showWeatherInfo(weather)

        } else {
            val weatherId = intent.getStringExtra("weather_id")
            weather_layout.visibility = View.INVISIBLE
            requestWeather(weatherId)
        }
        val bingPic = preferences.getString("bing_pic_img", null)
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg)

        } else {
            loadBingPic()
        }
    }

    private fun loadBingPic() {
        val requestBingPic = "http://guolin.tech/api/bing_pic"
        HttpUtil.sendOkHttpRequst(requestBingPic, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val bingPic = response?.body()?.string()
                val editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                editor.putString("bing_pic", bingPic)
                editor.apply()
                runOnUiThread(object : Runnable {
                    override fun run() {
                        Glide.with(this@WeatherActivity).load(bingPic).into(bingPicImg)
                    }
                })
            }
        })
    }

    private fun requestWeather(weatherId: String) {
        val weatherUri = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=b96e6305b42c45e6a54b52b6bace3867"
        HttpUtil.sendOkHttpRequst(weatherUri, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
                runOnUiThread(object : Runnable {
                    override fun run() {
                        Toast.makeText(this@WeatherActivity, "获取天气失败", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onResponse(call: Call?, response: Response?) {
                val response = response?.body()!!.string()
                val responseWeather = JsonHlr.halResponseWeather(response)
                runOnUiThread(object : Runnable {
                    override fun run() {
                        if (responseWeather != null && "ok" == responseWeather.status) {
                            val editor = PreferenceManager.getDefaultSharedPreferences(this@WeatherActivity).edit()
                            editor.putString("weather", response)
                            editor.apply()
                            showWeatherInfo(responseWeather)
                        } else {
                            Toast.makeText(this@WeatherActivity, "获取天气失败", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            }

        })

    }

    private fun showWeatherInfo(weather: Weather) {
        val cityName = weather.basic.cityName
        val updateTime = weather.basic.update.updateTime.split(" ")[1]
        val degree = weather.now.temperature + "℃"
        val weatherInfo = weather.now.more.info
        titleCity.text = cityName
        titleUpdateTime.text = updateTime
        degreeText.text = degree
        weatherInfoText.text = weatherInfo
        forecastLayout.removeAllViews()
        for (forecast in weather.forecastList) {

            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dataText = view.findViewById<TextView>(R.id.date_text)
            val infoText = view.findViewById<TextView>(R.id.info_text)
            val maxText = view.findViewById<TextView>(R.id.max_text)
            val minText = view.findViewById<TextView>(R.id.min_text)
            dataText.text = forecast.date
            infoText.text = forecast.more.info
            maxText.text = forecast.temperature.max
            minText.text = forecast.temperature.min
            forecastLayout.addView(view)
        }
        if (weather.aqi != null) {
            aqiText.text = weather.aqi.city.aqi
            pm25Text.text = weather.aqi.city.pm25
        }

        val comfort = "舒适度" + weather.suggestion.comfort.info
        val carWash = "洗车指数" + weather.suggestion.carWash.info
        val sport = "运动建议" + weather.suggestion.sport.info
        comfortText.text = comfort
        carWashText.text = carWash
        sportText.text = sport
        weatherLayout.visibility = View.VISIBLE
    }
}
