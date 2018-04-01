package com.example.libbliy.coolweather.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

/**
 * Created by libbliy on 2018/2/24.
 */
@Database(entities = [City::class, Province::class, County::class], version = 1)
abstract class CityDatabase : RoomDatabase() {
    abstract fun getDao(): AreaDao

    companion object {
        fun getInstance(context: Context): CityDatabase = Room.databaseBuilder(context.applicationContext, CityDatabase::class.java, "city.db").build()
    }
}
