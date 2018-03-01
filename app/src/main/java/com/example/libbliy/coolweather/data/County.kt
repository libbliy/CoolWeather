package com.example.libbliy.coolweather.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by libbliy on 2018/1/30.
 */
@Entity(tableName = "County")
data class County(var mCityId: Int , var mCountyName: String, var mWeatherId: String, @PrimaryKey var id: Int)