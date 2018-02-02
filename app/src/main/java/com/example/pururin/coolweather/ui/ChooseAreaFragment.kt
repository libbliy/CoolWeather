package com.example.pururin.coolweather.ui

import android.app.Fragment
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.pururin.coolweather.R
import com.example.pururin.coolweather.db.City
import com.example.pururin.coolweather.db.County
import com.example.pururin.coolweather.db.Province
import kotlinx.android.synthetic.main.choose_area.*
import org.litepal.crud.DataSupport

/**
 * Created by libbliy on 2018/2/1.
 */
class ChooseAreaFragment : Fragment() {

    private var progressDialog: ProgressDialog? = null
    private var titleText: TextView? = null
    private lateinit var backButton: Button
    private lateinit var listView: ListView


    private var adapter: ArrayAdapter<String>? = null
    private var dataList: List<String> = ArrayList<String>()

    private lateinit var provinceList: List<Province>
    private var cityList: List<City>? = null
    private var countyList: List<County>? = null

    private var selectedProvince: Province? = null
    private var selectedCity: City? = null
    private var selectedCounty: County? = null
    private var currentLevel: Int? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var view:View = inflater!!.inflate(R.layout.choose_area, container, false)
        titleText=view.findViewById(R.id.title_text)
        backButton=view.findViewById(R.id.back_button)
        listView=view.findViewById(R.id.list_view)
        adapter=ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,dataList)
        listView.adapter=adapter
        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        listView.onItemClickListener = object :AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (currentLevel == LENCEL_PROVINCE) {

                    selectedProvince= provinceList?.get(position)
                    queryCities()
                }else if (currentLevel == LENCEL_CITY) {
                    selectedCity= cityList?.get(position)
                    queryCounties()
                }
            }


        }
        backButton.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {
                if (currentLevel == LENCEL_COUNTY) {
                    queryCities()
                }else if (currentLevel == LENCEL_CITY) {
                    queryProvinces()
                }
            }

        })
        queryProvinces()



    }

    private fun queryProvinces() {
        titleText?.setText("中国")
        backButton.visibility=View.GONE
        provinceList=DataSupport.findAll(Province.class)

    }



    companion object {
        val LENCEL_PROVINCE = 0
        val LENCEL_CITY = 1
        val LENCEL_COUNTY = 2

    }


}