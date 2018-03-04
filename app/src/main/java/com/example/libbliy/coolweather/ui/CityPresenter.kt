package com.example.libbliy.coolweather.ui

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.libbliy.coolweather.data.City
import com.example.libbliy.coolweather.data.County
import com.example.libbliy.coolweather.data.Dao
import com.example.libbliy.coolweather.data.Province
import com.example.libbliy.coolweather.util.HttpUtil
import com.example.libbliy.coolweather.util.JsonHlr
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.Executor

class CityPresenter(val dao: Dao, val fragment: ChooseAreaFragment) {

    var dataList: MutableList<String> = ArrayList()

    private lateinit var provinceList: List<Province>
    private lateinit var cityList: List<City>
    private lateinit var countyList: List<County>

    private lateinit var selectedProvince: Province
    private lateinit var selectedCity: City
    private lateinit var selectedCounty: County
    var currentLevel: Int = 0

    init {
        fragment.cityPresenter = this
    }

    fun queryProvinces() {
        getAllProvince(1, object : Callback {
            override fun onMain(allProvince: List<Any>) {
                provinceList = allProvince as List<Province>
                if (provinceList.isNotEmpty()) {
                    dataList.clear()
                    provinceList.forEach { province -> dataList.add(province.mProvinceName) }
                    fragment.showProvinceList()
                    currentLevel = ChooseAreaFragment.LENCEL_PROVINCE
                } else {
                    val address = "http://guolin.tech/api/china"
                    queryFromServer(address, "province")
                }

            }

        })

    }

    fun queryCities() {
        getAllProvince(2, object : Callback {
            override fun onMain(allProvince: List<Any>) {
                cityList= allProvince as List<City>
                when {
                    cityList.isNotEmpty() -> {
                        dataList.clear()
                        cityList.forEach { cityList -> dataList.add(cityList.mCityName) }
                        fragment.showCityList(selectedProvince)
                        currentLevel = ChooseAreaFragment.LENCEL_CITY
                        Log.w("currentLevel",currentLevel.toString())
                    }
                    else -> {
                        val provinceCode = selectedProvince.mProvinceCode
                        val address = "http://guolin.tech/api/china/$provinceCode"
                        queryFromServer(address, "city")

                    }
                }
            }
        })
    }

    fun queryCounties() {
        getAllProvince(3, object : Callback {
            override fun onMain(allProvince: List<Any>) {
                countyList= allProvince as List<County>
                when {
                    countyList.isNotEmpty() -> {
                        dataList.clear()
                        countyList.forEach { dataList.add(it.mCountyName) }
                        fragment.showCountyList(selectedCity)
                        currentLevel = ChooseAreaFragment.LENCEL_COUNTY
                    }
                    else -> {
                        val provinceCode = selectedProvince.mProvinceCode
                        val cityCode = selectedCity.mCityCode
                        val address = "http://guolin.tech/api/china/$provinceCode/$cityCode"
                        queryFromServer(address, "county")
                    }
                }
            }
        })
    }

    private fun queryFromServer(address: String, type: String) {
        fragment.showProgressBar()

        HttpUtil.sendOkHttpRequest(
                address,
                object : okhttp3.Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
                        mainThread.execute {
                            fragment.showFail()
                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        Log.w("response", response.toString())
                        val responseText = response?.body()!!.string()
                        Log.w("response", responseText)
                        val result = when (type) {
                            "province" -> JsonHlr.hdlResponseProvince(responseText)
                            "city" -> JsonHlr.hdlResponseCity(responseText, selectedProvince.mProvinceCode)
                            "county" -> JsonHlr.hdlResponseCounty(responseText, selectedCity.mCityCode)
                            else -> false
                        }
                        if (result) {
                            //  closeProgressBar
                            mainThread.execute {

                                fragment.closeProgressBar()
                            }
                            when (type) {
                                "province" -> queryProvinces()
                                "city" -> queryCities()
                                "county" -> queryCounties()
                            }
                        }
                    }
                }
        )

    }

    private val mainThread = MainThreadExecutor()


    private fun getAllProvince(int: Int, callback: Callback) = Thread {
        val allProvince = when (int) {
            1 -> dao.getAllProvince()

            2 -> dao.getCity(selectedProvince.mProvinceCode)
            else -> dao.getCounty(selectedCity.mCityCode)
        }

        mainThread.execute {
            callback.onMain(
                    allProvince
            )

        }
    }.start()

    private class MainThreadExecutor : Executor {

        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }

    }

    private interface Callback {

        fun onMain(allProvince: List<Any>)

    }

    fun getWeatherId(position: Int) = countyList[position].mWeatherId

    fun toCountyList(position: Int) {
        Log.w("currentLevel","a")
        selectedCity = cityList[position]
        queryCounties()
    }

    fun toCityList(position: Int) {
        //Log.w("currentLevel",currentLevel.toString())
        selectedProvince = provinceList[position]
        queryCities()
    }
}
