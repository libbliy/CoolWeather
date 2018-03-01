package com.example.libbliy.coolweather

import android.content.Context
import com.example.libbliy.coolweather.data.CityDatabase

/**
 * Created by libbliy on 2018/2/26.
 */
object In {

    fun pr(context: Context): CityDatabase = CityDatabase.getInstance(context)
}