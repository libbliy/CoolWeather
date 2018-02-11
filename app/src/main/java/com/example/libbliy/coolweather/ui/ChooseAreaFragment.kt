package com.example.libbliy.coolweather.ui

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.libbliy.coolweather.R
import com.example.libbliy.coolweather.db.City
import com.example.libbliy.coolweather.db.County
import com.example.libbliy.coolweather.db.Province
import com.example.libbliy.coolweather.util.HttpUtil
import com.example.libbliy.coolweather.util.JsonHlr
import kotlinx.android.synthetic.main.activity_weather.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.litepal.crud.DataSupport
import java.io.IOException

/**
 * Created by libbliy on 2018/2/1.
 */
class ChooseAreaFragment : Fragment() {

    private lateinit var progressBar:ProgressBar //'ProgressDialog' is deprecated. Deprecated in Java
    private lateinit var titleText: TextView
    private lateinit var backButton: Button
    private lateinit var listView: ListView


    private lateinit var adapter: ArrayAdapter<String>
    private var dataList: MutableList<String> = ArrayList()

    private lateinit var provinceList: List<Province>
    private lateinit var cityList: List<City>
    private lateinit var countyList: List<County>

    private lateinit var selectedProvince: Province
    private lateinit var selectedCity: City
    private lateinit var selectedCounty: County
    private var currentLevel: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater!!.inflate(R.layout.choose_area, container, false)
        titleText = view.findViewById(R.id.title_text)
        backButton = view.findViewById(R.id.back_button)
        listView = view.findViewById(R.id.list_view)
        progressBar=view.findViewById(R.id.progress_bar)
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, dataList)
        listView.adapter = adapter
        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            if (currentLevel == LENCEL_PROVINCE) {

                selectedProvince = provinceList[position]
                queryCities()
            } else if (currentLevel == LENCEL_CITY) {
                selectedCity = cityList[position]
                queryCounties()
            }else if (currentLevel == LENCEL_COUNTY) {
                selectedCounty= countyList[position]
                val weatherId = selectedCounty.mWeatherId
                if (activity is MainActivity) {
                    val intent = Intent(activity, WeatherActivity::class.java)
                    intent.putExtra("weather_id",weatherId )
                    startActivity(intent)
                    activity.finish()
                }else if (activity is WeatherActivity) {
                    val activity = activity as WeatherActivity
                    activity.drawer_layout.closeDrawers()
                    activity.swipe_refresh.isRefreshing=true
                    activity.requestWeather(weatherId)
                }
            }
        }
        backButton.setOnClickListener {
            if (currentLevel == LENCEL_COUNTY) {
                queryCities()
            } else if (currentLevel == LENCEL_CITY) {
                queryProvinces()
            }
        }
        queryProvinces()


    }

    private fun queryProvinces() {
        titleText.text = "中国"
        backButton.visibility = View.GONE
        //DataSupport.deleteAll(Province::class.java)
        provinceList = DataSupport.findAll(Province::class.java)

        if (provinceList.isNotEmpty()) {
            dataList.clear()
            for (province in provinceList) {
                dataList.add(province.mProvinceName)
            }
            adapter.notifyDataSetChanged()
            listView.setSelection(0)
            currentLevel = LENCEL_PROVINCE
        } else {
            val address = "http://guolin.tech/api/china"
            queryFromServer(address, "province")

        }
    }

    private fun queryCities() {
        titleText.text = selectedProvince.mProvinceName
        backButton.visibility = View.VISIBLE
        cityList = DataSupport.where("mprovinceid=?", selectedProvince.mProvinceCode.toString()).find(City::class.java)
        if (cityList.isNotEmpty()) {
            dataList.clear()
            for (cityList in cityList) {
                dataList.add(cityList.mCityName)
            }
            adapter.notifyDataSetChanged()
            listView.setSelection(0)
            currentLevel = LENCEL_CITY
        } else {
            val provinceCode = selectedProvince.mProvinceCode
            val address = "http://guolin.tech/api/china/" + provinceCode
            queryFromServer(address, "city")

        }
    }

    private fun queryCounties() {
        titleText.text = selectedCity.mCityName
        backButton.visibility = View.VISIBLE
        countyList = DataSupport.where("mcityid=?", selectedCity.mCityCode.toString()).find(County::class.java)
        when {
            countyList.isNotEmpty() -> {
                dataList.clear()
                countyList.forEach { dataList.add(it.mCountyName) }
                adapter.notifyDataSetChanged()
                listView.setSelection(0)
                currentLevel = LENCEL_COUNTY
            }
            else -> {
                val provinceCode = selectedProvince.mProvinceCode
                val cityCode = selectedCity.mCityCode
                val address = "http://guolin.tech/api/china/$provinceCode/$cityCode"
                queryFromServer(address, "county")

            }
        }
    }

    private fun queryFromServer(address: String, type: String) {
        // showProgessBar
        progressBar.visibility=View.VISIBLE
        HttpUtil.sendOkHttpRequest(address, object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                activity.runOnUiThread {
                    // closeProgressBar
                    progressBar.visibility=View.GONE
                    Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                Log.w("response",response.toString())
                val responseText = response?.body()!!.string()
                Log.w("response", responseText)
                val result = when (type) {
                    "province" -> JsonHlr.hdlResponseProvince(responseText)
                    "city" -> JsonHlr.hdlResponseCity(responseText, selectedProvince.mProvinceCode)
                    "county" -> JsonHlr.hdlResponseCounty(responseText, selectedCity.mCityCode)
                    else -> false
                }
                if (result) activity.runOnUiThread {
                    //  closeProgressBar
                    progressBar.visibility=View.GONE
                    when (type) {
                        "province" -> queryProvinces()
                        "city" -> queryCities()
                        "county" -> queryCounties()
                    }
                }

            }


        })
    }


    companion object {
        const val LENCEL_PROVINCE = 0
        const val LENCEL_CITY = 1
        const val LENCEL_COUNTY = 2

    }


}