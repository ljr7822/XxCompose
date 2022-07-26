package com.example.xxcompose.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.xxcompose.bean.NewslistItem

/**
 * 数据库操作类
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose
 */
@Dao
interface NewslistItemDao {

    @Query("SELECT * FROM newslistitem")
    suspend fun getAll(): List<NewslistItem>

    @Insert
    suspend fun insertAll(newsItem: List<NewslistItem>?)

    @Query("DELETE FROM newslistitem")
    suspend fun delete()
}