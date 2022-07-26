package com.example.xxcompose

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.example.xxcompose.db.AppDatabase
import dagger.hilt.android.HiltAndroidApp

/**
 * Application基类
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/19
 * @Package: com.example.xxcompose
 */
@HiltAndroidApp
class XxApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var db: AppDatabase
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: XxApplication
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        db = AppDatabase.getInstance(context = context)
        instance = this
    }
}