package com.example.libbliy.coolweather.db

/**
 * Created by libbliy on 2018/1/30.
 */
class City : DataSupport() {
    var id: Int = 0
    lateinit var mCityName: String
    var mCityCode: Int = 0

    var mProvinceId: Int = 0
}