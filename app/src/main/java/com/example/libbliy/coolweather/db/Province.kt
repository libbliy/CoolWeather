package com.example.libbliy.coolweather.db

import org.litepal.crud.DataSupport

/**
 * Created by libbliy on 2018/1/30.
 */
class Province : DataSupport() {
    var id: Int=0
    lateinit var mProvinceName:String
    var  mProvinceCode:Int=0
}