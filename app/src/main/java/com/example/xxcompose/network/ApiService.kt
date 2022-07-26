package com.example.xxcompose.network

import com.example.xxcompose.bean.XxNewBean
import com.example.xxcompose.network.Constant.API_KEY
import retrofit2.Call
import retrofit2.http.GET

/**
 * APi服务接口
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose.network
 */
interface ApiService {

    /**
     * 获取新闻数据
     */
    @GET("/internet/index?key=$API_KEY")
    fun getNewList(): Call<XxNewBean>
}