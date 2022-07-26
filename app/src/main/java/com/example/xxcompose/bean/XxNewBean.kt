package com.example.xxcompose.bean

/**
 * 请求返回体
 */
data class XxNewBean(
    // 请求返回信息
    var msg: String = "",
    // 状态码
    var code: Int = 0,
    // 新闻实体列表
    var newslist: List<NewslistItem>?
)