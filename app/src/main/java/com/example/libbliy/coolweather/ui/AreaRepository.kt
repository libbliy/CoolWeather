package com.example.libbliy.coolweather.ui

import android.util.Log
import com.example.libbliy.coolweather.data.AreaDao
import com.example.libbliy.coolweather.data.City
import com.example.libbliy.coolweather.data.County
import com.example.libbliy.coolweather.data.Province
import com.example.libbliy.coolweather.util.HttpUtil
import com.example.libbliy.coolweather.util.JsonHlr
import okhttp3.Call
import okhttp3.Response
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.IOException

/**
 * Created by libbly on 2018/3/21.
 */
class AreaRepository(
        val areaDao: AreaDao
) {

    var mProvinceCode = 0
    var mCityCode = 0

    fun getAllProvince(myCallback: MyCallback) = doAsync {

        val allProvince = areaDao.getAllProvince()

        val list = mutableListOf<Province>()

        when {
            allProvince.isNotEmpty() ->
                list.apply {
                    allProvince.forEach {
                        add(it)
//                        Log.w("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKK", it.mProvinceName)
                    }
                }
            else -> {
                val address = "http://guolin.tech/api/china"
                queryFromServer(address, "province", myCallback)
            }
        }
        uiThread {
            myCallback.onMain(list)
            Log.w("okokokok", list.toString())
        }
    }

    fun getCity(provinceId: Int, myCallback: MyCallback) = doAsync {

        mProvinceCode = provinceId

        val city = areaDao.getCity(provinceId)

        val list = mutableListOf<City>()

        when {
            city.isNotEmpty() -> list.apply { city.forEach { add(it) } }

            else -> {
                val address = "http://guolin.tech/api/china/$provinceId"
                queryFromServer(address, "city", myCallback)
            }
        }

        uiThread {
            myCallback.onMain(list)
            Log.w("okokokok", list.toString())
        }
    }

    fun getCounty(cityId: Int, myCallback: MyCallback) = doAsync {

        mCityCode = cityId

        val list = mutableListOf<County>()

        val county = areaDao.getCounty(cityId)
        when {
            county.isNotEmpty() -> list.apply { county.forEach { add(it) } }
            else -> {
                val address = "http://guolin.tech/api/china/$mProvinceCode/$cityId"
                queryFromServer(address, "county", myCallback)
            }
        }

        uiThread {
            myCallback.onMain(list)
        }
    }

    private fun queryFromServer(address: String, type: String, myCallback: MyCallback) = doAsync {
        //        fragment.showProgressBar()
        HttpUtil.sendOkHttpRequest(
                address,
                object : okhttp3.Callback {
                    override fun onFailure(call: Call?, e: IOException?) {
//                        mainThread.execute {
//                            fragment.showFail()
//                        }
                    }

                    override fun onResponse(call: Call?, response: Response?) {
                        Log.w("response", response.toString())
                        val responseText = response?.body()!!.string()
                        Log.w("response", responseText)
                        val result = when (type) {
                            "province" -> JsonHlr.hdlResponseProvince(responseText)
                            "city" -> JsonHlr.hdlResponseCity(responseText, mProvinceCode)
                            "county" -> JsonHlr.hdlResponseCounty(responseText, mCityCode)
                            else -> false
                        }
                        if (result) {
                            val list = when (type) {
                                "province" -> areaDao.getAllProvince()
                                "city" -> areaDao.getCity(mProvinceCode)
                                "county" -> areaDao.getCounty(mCityCode)
                                else -> listOf()
                            }

                            uiThread { myCallback.onMain(list) }

                            //  closeProgressBar
//                            mainThread.execute {
//
//                                fragment.closeProgressBar()
//                            }
//                            when (type) {
//                                "province" -> queryProvinces()
//                                "city" -> queryCities()
//                                "county" -> queryCounties()
//                            }
                        }
                    }
                }
        )
    }

    interface MyCallback {

        fun onMain(areaList: List<Any>)
    }
}
