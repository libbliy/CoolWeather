package com.example.libbliy.coolweather.util

import android.util.Log
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
            val request = Request.Builder().url(address).build()
            Log.w("request",request.toString())
            okHttpClient.newCall(request).enqueue(callback)




        }
    }
}