package com.example.xxcompose.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose.network
 */
object ServiceCreator {

    private const val baseUrl = "http://api.tianapi.com"

    private fun getRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun <T> create(serviceClass: Class<T>): T = getRetrofit().create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)
}