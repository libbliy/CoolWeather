package com.example.libbliy.coolweather.gson

import com.google.gson.annotations.SerializedName

/**
 * Created by libbliy on 2018/2/3.
 */
class Now {
    @SerializedName("tmp")
    lateinit var temperature: String
    @SerializedName("cond")
    lateinit var more: More

    class More {
        @SerializedName("txt")
        lateinit var info: String
    }
}