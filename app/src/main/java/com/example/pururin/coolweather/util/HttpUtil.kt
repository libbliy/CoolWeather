package com.example.pururin.coolweather.util

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by libbliy on 2018/2/1.
 */
class HttpUtil {
    companion object {
        fun sendOkHttpRequst(address: String,callback: Callback) {
            val okHttpClient = OkHttpClient()
            var request = Request.Builder().url(address).build()
            okHttpClient.newCall(request).enqueue(callback)




        }
    }
}