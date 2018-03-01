package com.example.libbliy.coolweather.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by libbliy on 2018/2/24.
 */
@Dao interface Dao {
    @Query("SELECT * FROM Province") fun getAllProvince(): List<Province>

    @Query("SELECT * FROM City WHERE mProvinceId = :arg0") fun getCity(provinceId: Int): List<City>

    @Query("SELECT * FROM County WHERE mCityId = :arg0") fun getCounty(cityId: Int): List<County>

    @Insert fun insertCity(city: City)

    @Insert fun insertCounty(county: County)

    @Insert fun insertProvince(province: Province)
}