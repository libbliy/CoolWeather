package com.example.libbliy.coolweather.util

import android.util.Log
import com.example.libbliy.coolweather.data.AreaDao
import com.example.libbliy.coolweather.data.City
import com.example.libbliy.coolweather.data.County
import com.example.libbliy.coolweather.data.Province
import com.example.libbliy.coolweather.gson.Weather
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by libbliy on 2018/2/1.
 */
class JsonHlr {

    companion object {
        lateinit var areaDao: AreaDao

        fun hdlResponseProvince(response: String): Boolean {

            if (!response.isEmpty()) {
                val allProvince = JSONArray(response)
                Log.w("jsonArray", allProvince.toString())
                try {
                    for (i in 0 until allProvince.length()) {
                        val provinceObject = allProvince.getJSONObject(i)
                        val province = Province(provinceObject.getString("name"), provinceObject.getInt("id"))
                        areaDao.insertProvince(province)
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun hdlResponseCity(response: String, provinceId: Int): Boolean {
            if (!response.isEmpty()) {
                val allCities = JSONArray(response)
                try {
                    for (i in 0 until allCities.length()) {
                        val cityObject = allCities.getJSONObject(i)
                        val city = City(provinceId, cityObject.getString("name"), cityObject.getInt("id"))
                        areaDao.insertCity(city)
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun hdlResponseCounty(response: String, cityId: Int): Boolean {
            if (!response.isEmpty()) {
                val allCounties = JSONArray(response)
                try {
                    for (i in 0 until allCounties.length()) {
                        val countyObject = allCounties.getJSONObject(i)
                        val county = County(cityId
                                , countyObject.getString("name")
                                , countyObject.getString("weather_id")
                        , countyObject.getInt("id")
                        )
                        areaDao.insertCounty(county)
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false
        }

        fun halResponseWeather(response: String): Weather {
            Log.w("weatherResponse", response)
            val jsonObject = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray("HeWeather")
            val weatherContent = jsonArray.getJSONObject(0).toString()
            Log.w("weatherCOUNT", weatherContent)
            return Gson().fromJson(weatherContent, Weather::class.java)
        }
    }
}