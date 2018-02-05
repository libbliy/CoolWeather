package com.example.libbliy.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by libbliy on 2018/2/3.
 */
class Base {
    @SerializedName("city")
    lateinit var cityName: String

    @SerializedName("id")
    lateinit var weatherId: String

    lateinit var update: Update

    class Update {
        @SerializedName("loc")
        lateinit var updateTime: String
    }
}