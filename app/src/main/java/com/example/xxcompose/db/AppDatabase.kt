package com.example.xxcompose.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.xxcompose.bean.NewslistItem

/**
 * room数据库配置类
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose
 */
@Database(entities = [NewslistItem::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsItemDao(): NewslistItemDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        private const val DATABASE_NAME = "good_news.db"

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                    .allowMainThreadQueries().build().also { instance = it }
            }
        }
    }
}
