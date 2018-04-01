package com.example.libbliy.coolweather.ui

import android.content.Context
import com.example.libbliy.coolweather.data.CityDatabase
import com.example.libbliy.coolweather.util.JsonHlr.Companion.areaDao

/**
 * Created by libbly on 2018/3/25.
 */
class Human(context: Context) {
    val cityDatabase = CityDatabase.getInstance(context)
    val dao = cityDatabase.getDao()

    init {
        areaDao = dao
    }

    val areaRepository = AreaRepository(dao)
    val myViewModelProvider = MyViewModelProvider(areaRepository)
}