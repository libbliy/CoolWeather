package com.example.libbliy.coolweather.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.preference.PreferenceManager
import com.example.libbliy.coolweather.util.HttpUtil
import com.example.libbliy.coolweather.util.JsonHlr
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class AutoUpdateService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        updateWeather()
        updateBingPic()
        val manager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val anHour = 8 * 60 * 60 * 1000 //8小时的毫秒数
        val triggerAtTime = SystemClock.elapsedRealtime() + anHour
        val intent1 = Intent(this, AutoUpdateService::class.java)
        val pendingIntent = PendingIntent.getService(this, 0, intent1, 0)
        manager.cancel(pendingIntent)
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pendingIntent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun updateBingPic() {
        val requestBingPic = "http://guolin.tech/api/bing_pic"
        HttpUtil.sendOkHttpRequest(requestBingPic,object: Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                e?.printStackTrace()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val bingPic = response?.body()!!.string()
                val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                editor.putString("bing_pic",bingPic)
                editor.apply()
            }
        })
    }

    private fun updateWeather() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val weatherString = sharedPreferences.getString("weather", null)
        if (weatherString != null) {
            val weather = JsonHlr.halResponseWeather(weatherString)
            val weatherId = weather.basic.weatherId

            val weatherUrl = "http://guolin.tech/api/weather?cityid=$weatherId&key=b96e6305b42c45e6a54b52b6bace3867"
            HttpUtil.sendOkHttpRequest(weatherUrl,object: Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    e?.printStackTrace()
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val responseText = response?.body()!!.string()
                    val weather = JsonHlr.halResponseWeather(responseText)
                    if ("ok" == weather.status) {
                        val editor = PreferenceManager.getDefaultSharedPreferences(this@AutoUpdateService).edit()
                        editor.putString("weather",responseText)
                        editor.apply()
                    }
                }
            })
        }
    }
}
