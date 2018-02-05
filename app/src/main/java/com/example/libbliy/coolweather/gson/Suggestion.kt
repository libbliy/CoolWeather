package com.example.libbliy.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by libbliy on 2018/2/3.
 */
class Suggestion {
    @SerializedName("comf")
    lateinit var comfort: Comfort
    @SerializedName("cw")
    lateinit var carWash: CarWash

    class CarWash {
        @SerializedName("txt")
        lateinit var info: String

    }

    lateinit var sport: Sport

    class Sport {
        @SerializedName("txt")
        lateinit var info: String

    }

    class Comfort {
        @SerializedName("txt")
        lateinit var info: String
    }

}