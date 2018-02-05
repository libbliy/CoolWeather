package com.example.libbliy.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by libbliy on 2018/2/4.
 */
class Weather {
    lateinit var status: String
    lateinit var base: Base
    lateinit var aqi: AQI
    lateinit var now: Now
    lateinit var suggestion: Suggestion
    @SerializedName("daily_forecast")
    lateinit var forecastList: MutableList<Forecast>
}