package com.example.libbliy.coolweather.util

import android.util.Log
import com.example.libbliy.coolweather.db.City
import com.example.libbliy.coolweather.db.County
import com.example.libbliy.coolweather.db.Province
import com.example.libbliy.coolweather.gson.Weather
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by libbliy on 2018/2/1.
 */
class JsonHlr {

    companion object {

        fun hdlResponseProvince(response: String): Boolean {

            if (!response.isEmpty()) {
                val allProvince = JSONArray(response)
                Log.w("jsonArray",allProvince.toString())
                try {
                    for (i in 0 until allProvince.length()) {
                        val provinceObject = allProvince.getJSONObject(i)
                        Log.w("object",provinceObject.toString())
                        val province = Province()
                        province.mProvinceName = provinceObject.getString("name")
                        province.mProvinceCode = provinceObject.getInt("id")
                        province.save()

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
                val allCitties = JSONArray(response)
                try {
                    for (i in 0 until allCitties.length()) {
                        val cityObject = allCitties.getJSONObject(i)
                        val city = City()
                        city.mCityName = cityObject.getString("name")
                        city.mCityCode = cityObject.getInt("id")
                        city.mProvinceId = provinceId
                        city.save()
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
                        val county = County()
                        county.mCountyName = countyObject.getString("name")
                        county.mWeatherId = countyObject.getString("weather_id")
                        county.mCityId = cityId
                        county.save()
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false


        }

        fun halResponseWeather(response: String): Weather {
            Log.w("weatherResponse",response)
            val jsonObject = JSONObject(response)
            val jsonArray = jsonObject.getJSONArray("HeWeather")
            val weatherContent = jsonArray.getJSONObject(0).toString()
            Log.w("weatherCOUNT",weatherContent)
            return Gson().fromJson(weatherContent, Weather::class.java)
        }


    }
}