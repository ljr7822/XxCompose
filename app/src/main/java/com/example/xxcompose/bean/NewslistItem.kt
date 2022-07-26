package com.example.xxcompose.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 每条新闻实体
 */
@Entity
data class NewslistItem(
    // 封面图url
    val picUrl: String = "",
    // 时间戳
    val ctime: String = "",
    // 描述
    val description: String = "",
    // id 主键
    @PrimaryKey val id: String = "",
    // 资源
    val source: String = "",
    // 标题
    val title: String = "",
    // 跳转链接
    val url: String = ""
)