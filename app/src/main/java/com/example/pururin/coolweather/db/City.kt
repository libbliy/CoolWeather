package com.example.pururin.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by libbliy on 2018/1/30.
 */
class City : DataSupport() {
    var id: Int? = null
    lateinit var mCityName:String
    var mCityCode:Int=0

    var mProvinceId:Int?=null





}