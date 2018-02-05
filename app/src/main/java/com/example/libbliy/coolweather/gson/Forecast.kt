package com.example.libbliy.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by libbliy on 2018/2/3.
 */
class Forecast {
    lateinit var date: String

    @SerializedName("tmp")
    lateinit var temperature: Temperature

    class Temperature {

        lateinit var max: String
        lateinit var min: String
    }

    @SerializedName("cond")
    lateinit var more: More

    class More {
        @SerializedName("txt_d")
        lateinit var info: String

    }
}