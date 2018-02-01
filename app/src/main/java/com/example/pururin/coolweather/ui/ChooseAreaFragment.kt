package com.example.pururin.coolweather.ui

import android.app.Fragment
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.pururin.coolweather.R
import com.example.pururin.coolweather.db.City
import com.example.pururin.coolweather.db.County
import com.example.pururin.coolweather.db.Province
import kotlinx.android.synthetic.main.choose_area.*

/**
 * Created by libbliy on 2018/2/1.
 */
class ChooseAreaFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null
    private var titleText: TextView? = null
    private var backButton: Button? = null
    private lateinit var listView: ListView


    private var adapter: ArrayAdapter<String>? = null
    private var dataList: List<String> = ArrayList<String>()

    private var provinceList: List<Province>? = null
    private var cityList: List<City>? = null
    private var countyList: List<County>? = null

    private var selectedProvince: Province? = null
    private var selectedCity: Province? = null
    private var selectedCounty: Province? = null
    private var currentLevel: Int? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view = inflater!!.inflate(R.layout.choose_area, container, false)
        titleText=view.findViewById(R.id.title_text)
        backButton=view.findViewById(R.id.back_button)
        listView=view.findViewById(R.id.list_view)
        adapter=ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,dataList)
        
        return super.onCreateView(inflater, container, savedInstanceState)

    }


    companion object {
        val LENCEL_PROVINCE = 0
        val LENCEL_CITY = 1
        val LENCEL_COUNTY = 2

    }


}