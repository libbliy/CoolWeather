package com.example.libbliy.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by libbliy on 2018/1/30.
 */
class County : DataSupport() {
    var id: Int=0
    lateinit var mCountyName:String
    lateinit var mWeatherId:String
    var mCityId:Int=0




}