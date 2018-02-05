package com.example.libbliy.coolweather.gson

/**
 * Created by libbliy on 2018/2/3.
 */
class AQI {
    lateinit var city: AQICity

    class AQICity {
        lateinit var api:String
        lateinit var pm25:String
    }
}