package com.example.libbliy.coolweather.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

/**
 * Created by libbly on 2018/3/25.
 */
class MyViewModelProvider(
        private val areaRepository: AreaRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            with(modelClass) {
                isAssignableFrom(AreaRepository::class.java)
                ChooseAreaViewModel(areaRepository)
            } as T
}