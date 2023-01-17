package com.hao.hweather2.utils

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private var sharedPreferences : SharedPreferences ? = null
    private const val SHARED_NAME = "STATE_APP"
    private const val SHARED_FIRSt_LAUNCH = "LAUNCH"
    private const val SHARED_LANG = "LANG"
    private const val SHARED_TEMP = "TEMP"

    fun getInstance(context: Context) : SharedPreferences?
    {
        if (sharedPreferences == null)
        {
            sharedPreferences = context.getSharedPreferences(SHARED_NAME,Context.MODE_PRIVATE)
        }
        return sharedPreferences
    }

    fun getFirstLaunch(context: Context) : Int
    {
        return getInstance(context)!!.getInt(SHARED_FIRSt_LAUNCH,0)
    }

    fun setFirsLaunch(context: Context, unit : Int)
    {
        getInstance(context)!!.edit().putInt(SHARED_FIRSt_LAUNCH,unit).commit()
    }

    fun getTempUnit(context: Context) : Int
    {
        return getInstance(context)!!.getInt(SHARED_TEMP,0)
    }

    fun setTempUnit(context: Context, unit : Int)
    {
        getInstance(context)!!.edit().putInt(SHARED_TEMP,unit).commit()
    }

    fun getLanguage(context: Context) : String
    {
        return getInstance(context)?.getString(SHARED_LANG,"vi").toString()
    }

    fun setLanguage(context: Context, unit : String)
    {
        getInstance(context)!!.edit().putString(SHARED_LANG,unit).commit()
    }

}