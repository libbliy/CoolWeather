package com.example.libbliy.coolweather.ui

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.libbliy.coolweather.R
import com.example.libbliy.coolweather.data.City
import com.example.libbliy.coolweather.data.Province
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * Created by libbliy on 2018/2/1.
 */
class ChooseAreaFragment : Fragment() {

    lateinit var progressBar: ProgressBar //'ProgressDialog' is deprecated. Deprecated in Java
    private lateinit var titleText: TextView
    private lateinit var backButton: Button
    private lateinit var listView: ListView

    private lateinit var adapter: ArrayAdapter<String>

    lateinit var cityPresenter: CityPresenter

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view: View = inflater!!.inflate(R.layout.choose_area, container, false)
        titleText = view.findViewById(R.id.title_text)
        backButton = view.findViewById(R.id.back_button)
        listView = view.findViewById(R.id.list_view)
        progressBar = view.findViewById(R.id.progress_bar)
        adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, cityPresenter.dataList)
        listView.adapter = adapter
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        okayClick()
        cityPresenter.queryProvinces()
    }

    private fun okayClick() {
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            when (cityPresenter.currentLevel) {
                LENCEL_PROVINCE -> {
                    cityPresenter.toCityList(position)
                }
                LENCEL_CITY -> {
                    cityPresenter.toCounyList(position)
                }
                LENCEL_COUNTY -> {

                    val weatherId = cityPresenter.getWeatherId(position)
                    if (activity is MainActivity) {
                        val intent = Intent(activity, WeatherActivity::class.java)
                        intent.putExtra("weather_id", weatherId)
                        startActivity(intent)
                        activity.finish()
                    } else if (activity is WeatherActivity) {
                        val activity = activity as WeatherActivity
                        activity.drawer_layout.closeDrawers()
                        activity.swipe_refresh.isRefreshing = true
                        activity.requestWeather(weatherId)
                    }
                }
            }
        }
        backButton.setOnClickListener {
            if (cityPresenter.currentLevel == LENCEL_COUNTY) {
                cityPresenter.queryCities()
            } else if (cityPresenter.currentLevel == LENCEL_CITY) {
                cityPresenter.queryProvinces()
            }
        }
    }

    fun showProvinceList() {
        titleText.text = "中国"
        backButton.visibility = View.GONE
        adapter.notifyDataSetChanged()
        listView.setSelection(0)
    }
     fun showCityList(selectedProvince: Province) {
        titleText.text = selectedProvince.mProvinceName
        backButton.visibility = View.VISIBLE
         adapter.notifyDataSetChanged()
        listView.setSelection(0)
    }
    fun showCouryList(selectedCity: City) {
        titleText.text = selectedCity.mCityName
        backButton.visibility = View.VISIBLE
        adapter.notifyDataSetChanged()
        listView.setSelection(0)
    }
    fun showFail() {
        closeProgressBar()
        Toast.makeText(context, "加载失败", Toast.LENGTH_SHORT).show()
    }

    fun closeProgressBar() {
        progressBar.visibility = View.GONE

    }

    fun showProgessBar() {

        progressBar.visibility = View.VISIBLE
    }

    companion object {
        const val LENCEL_PROVINCE = 0
        const val LENCEL_CITY = 1
        const val LENCEL_COUNTY = 2

        const val ARGUMENT_EDIT_TASK_ID = "EDIT_TASK_ID"

        fun newInstance(taskId: String?) =
                ChooseAreaFragment().apply {
                    arguments = Bundle().apply {
                        putString(ChooseAreaFragment.ARGUMENT_EDIT_TASK_ID, taskId)
                    }
                }
    }
}