package com.example.libbliy.coolweather.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import android.view.View
import com.example.libbliy.coolweather.data.City
import com.example.libbliy.coolweather.data.County
import com.example.libbliy.coolweather.data.Province

/**
 * Created by libbly on 2018/3/20.
 */
class ChooseAreaViewModel(val areaRepository: AreaRepository) : ViewModel() {
    val areaListData = MutableLiveData<List<String>>()
    val tiltle = MutableLiveData<String>()
    val flag = MutableLiveData<Int>()
    val visibility = MutableLiveData<Boolean>()

    private lateinit var selectedProvince: Province
    private lateinit var selectedCity: City
    private lateinit var selectedCounty: County

    lateinit var allProvince: List<Province>

    lateinit var allCity: List<City>

    lateinit var allCounty: List<County>

    lateinit var intentFun: (String) -> Unit

    lateinit var weatherId: String
    fun start() {
//        areaListData.value= listOf("a","b")
//        allProvince = listOf(Province("a", 11), Province("b", 212))
        visibility.value = true
        flag.value = 0
        areaRepository.getAllProvince(object : AreaRepository.MyCallback {
            override fun onMain(areaList: List<Any>) {
                allProvince = areaList as List<Province>
                areaListData.value = (areaList as List<Province>).let { toStringList(it) }
                visibility.value = false
                Log.w("QQQQQQQQQQQKKKKKKKKKKKKKKKKKKKKKKKK", (areaList as List<Province>).toString())
            }
        })
    }

    fun getAdapter(list: List<String>) = AreaListAdapter(list) { view, i ->
        areaListData.value = listOf()
        visibility.value = true
        when (flag.value) {
            ChooseAreaFragment.LENCEL_PROVINCE -> {
                selectedProvince = allProvince[i]
                areaRepository.getCity(
                        selectedProvince.mProvinceCode,
                        object : AreaRepository.MyCallback {
                            override fun onMain(areaList: List<Any>) {
                                allCity = areaList as List<City>
                                areaListData.value = (areaList as List<City>).let { toStringList(it) }
                                visibility.value = false
                            }
                        })
                tiltle.value = selectedProvince.mProvinceName
                flag.value = 1
                Log.w("AAAAAAAAAAAAAAAAAAAAAAAAAAAAA", areaListData.value.toString())
            }
            ChooseAreaFragment.LENCEL_CITY -> {
                selectedCity = allCity[i]
                areaRepository.getCounty(selectedCity.mCityCode,
                        object : AreaRepository.MyCallback {
                            override fun onMain(areaList: List<Any>) {
                                allCounty = areaList as List<County>
                                areaListData.value = (areaList as List<County>).let { toStringList(it) }
                                visibility.value = false
                            }
                        })
                tiltle.value = selectedCity.mCityName
                flag.value = 2
            }
            ChooseAreaFragment.LENCEL_COUNTY -> {
                selectedCounty = allCounty[i]
                weatherId = selectedCounty.mWeatherId
                flag.value = 3

//                val weatherId = cityPresenter.getWeatherId(position)
//                if (activity is MainActivity) {
//                    val intent = Intent(activity, WeatherActivity::class.java)
//                    intent.putExtra("weather_id", weatherId)
//                    startActivity(intent)
//                    activity.finish()
//                } else if (activity is WeatherActivity) {
//                    val activity = activity as WeatherActivity
//                    activity.drawer_layout.closeDrawers()
//                    activity.swipe_refresh.isRefreshing = true
//                    activity.requestWeather(weatherId)
//                }
            }
        }
    }

    fun backButtonListenter(flag: Int) = when (flag) {
        ChooseAreaFragment.LENCEL_COUNTY -> View.OnClickListener { }
        ChooseAreaFragment.LENCEL_CITY -> View.OnClickListener { }
        else -> View.OnClickListener { }
    }

    fun toStringList(list: List<Any>): List<String> {
        val mutableList = mutableListOf<String>()

        if (list.isEmpty()) {
            return mutableList
        }

        return when (list.component1()) {
            is Province -> {
                list as List<Province>
                mutableList.apply { list.forEach { add(it.mProvinceName) } }
            }
            is City -> {
                list as List<City>
                mutableList.apply { list.forEach { add(it.mCityName) } }
            }
            else -> {
                list as List<County>
                mutableList.apply { list.forEach { add(it.mCountyName) } }
            }
        }
    }
}