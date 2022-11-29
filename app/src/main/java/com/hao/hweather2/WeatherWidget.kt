package com.hao.hweather2

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.lifecycle.ProcessLifecycleOwner
import com.hao.hweather2.utils.MySharedPreferences
import com.hao.hweather2.viewmodels.MainViewModel
import javax.inject.Inject


class WeatherWidget : AppWidgetProvider()  {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {

        if (appWidgetIds != null) {

            for ( appWidgetId in appWidgetIds)
            {
                val intent = Intent(context,MainActivity::class.java)

                val pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
                val remoteViews = RemoteViews(context?.packageName,R.layout.example_widget)

                remoteViews.setOnClickPendingIntent(R.id.tv_name,pendingIntent)
                remoteViews.setOnClickPendingIntent(R.id.tv_temperature,pendingIntent)
                remoteViews.setOnClickPendingIntent(R.id.humidity,pendingIntent)
                remoteViews.setOnClickPendingIntent(R.id.tv_Des,pendingIntent)

                appWidgetManager?.updateAppWidget(appWidgetId,remoteViews)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {

        mainViewModel = (context as MainActivity).mainViewModel

        val policy = ThreadPolicy.Builder().permitNetwork().build()
        StrictMode.setThreadPolicy(policy)

        super.onReceive(context, intent)

        val appWidgetManager = AppWidgetManager.getInstance(context)
        val remoteViews = RemoteViews(context.packageName, R.layout.example_widget)
        val watchWigdet = ComponentName(context,WeatherWidget::class.java)
        val lang = MySharedPreferences.getLanguage(context)
        val unit = MySharedPreferences.getTempUnit(context)
        val data = mainViewModel.getTopRecord()
        mainViewModel.getOneWeather(data.city.name,lang)
        mainViewModel.dataWeatherCity.observe(ProcessLifecycleOwner.get()) {
            remoteViews.setTextViewText(R.id.tv_name, it.city.name)
            if (unit == 0)
            {
                val temp = ((it.list[2].main.temp - 273).toInt()).toString()
                remoteViews.setTextViewText(R.id.tv_temperature,temp)
            }else
            {
                val temp = ((1.8 * it.list[2].main.temp - 459.67).toInt()).toString()
                remoteViews.setTextViewText(R.id.tv_temperature,temp)
            }

            remoteViews.setTextViewText(R.id.tv_humidity,"${it.list[2].main.humidity}%")
            remoteViews.setTextViewText(R.id.tv_Des,it.list[2].weather[0].description)
            appWidgetManager.updateAppWidget(watchWigdet, remoteViews);
        }
    }

}