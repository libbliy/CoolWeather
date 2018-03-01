package com.example.libbliy.coolweather.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by libbliy on 2018/1/30.
 */
@Entity(tableName = "City")
data class City(var mProvinceId: Int, var mCityName: String, @PrimaryKey var mCityCode: Int )
