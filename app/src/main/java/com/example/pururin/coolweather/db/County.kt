package com.example.pururin.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by libbliy on 2018/1/30.
 */
class County : DataSupport() {
    var id: Int? = null
    lateinit var mCountyName:String
    var mWeatherId:String?=null
    var mCityId:Int?=null




}