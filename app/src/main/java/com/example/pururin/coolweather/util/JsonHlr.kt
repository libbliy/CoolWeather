package com.example.pururin.coolweather.util

import com.example.pururin.coolweather.db.City
import com.example.pururin.coolweather.db.County
import com.example.pururin.coolweather.db.Province
import org.json.JSONArray

/**
 * Created by libbliy on 2018/2/1.
 */
class JsonHlr {
    companion object {
        fun hdlResponseProvince(response: String): Boolean {
            if (!response.isEmpty()) {
                val allProvince = JSONArray(response)
                try {
                    for (i in 0 until allProvince.length()) {
                        val provinceObject = allProvince.getJSONObject(i)
                        val province = Province()
                        province.mProvinceName=provinceObject.getString("name")
                        province.mProvinceCode=provinceObject.getInt("id")
                        province.save()
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false


        }  fun hdlResponseCity(response: String,provinceId:Int): Boolean {
            if (!response.isEmpty()) {
                val allCitties = JSONArray(response)
                try {
                    for (i in 0 until allCitties.length()) {
                        val cityObject = allCitties.getJSONObject(i)
                        val city = City()
                        city.mCityName=cityObject.getString("name")
                        city.mCityCode=cityObject.getInt("id")
                        city.mProvinceId=provinceId
                        city.save()
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false


        }  fun hdlResponseCounty(response: String,cityId:Int): Boolean {
            if (!response.isEmpty()) {
                val allCounties = JSONArray(response)
                try {
                    for (i in 0 until allCounties.length()) {
                        val countyObject = allCounties.getJSONObject(i)
                        val county = County()
                        county.mCountyName=countyObject.getString("name")
                        county.mWeatherId=countyObject.getString("weather_id")
                        county.mCityId=cityId
                        county.save()
                    }
                    return true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return false


        }

    }
}