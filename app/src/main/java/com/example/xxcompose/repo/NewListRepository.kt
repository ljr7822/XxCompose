package com.example.xxcompose.repo

import android.util.Log
import com.example.xxcompose.XxApplication
import com.example.xxcompose.bean.NewslistItem
import com.example.xxcompose.bean.XxNewBean
import com.example.xxcompose.network.Constant.CODE
import com.example.xxcompose.network.Constant.SUCCESS
import com.example.xxcompose.network.NetworkRequest
import com.example.xxcompose.utlis.XxEasyDataStore
import com.example.xxcompose.utlis.XxEasyDate
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.android.scopes.ViewScoped
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * 数据仓库类
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose.repo
 */
// 定义NewListRepository的作用域为@ViewModelScoped，并且注入
@ViewModelScoped
class NewListRepository @Inject constructor() : BaseRepository() {

    companion object {
        const val TAG = "NewListRepository"

        // 今日请求接口返回数据的时间戳
        const val REQUEST_TIMESTAMP = "requestTimestamp_news"

        lateinit var newslist: XxNewBean
    }


    /**
     * 请求新闻数据
     */
    fun getNewList(isRefresh: Boolean) = fire(Dispatchers.IO) {
        // 当前没有刷新并且不是今天第一次请求网络，则从本地获取
        if (!isRefresh && XxEasyDate.timestamp <= XxEasyDataStore.getData(
                REQUEST_TIMESTAMP,
                1649049670500
            )
        ) {
            // 当前时间未超过次日0点，从本地获取数据库
            Log.d(TAG, "load data from local.")
            newslist = getLocalForNews()
        } else {
            // 从网络中获取
            Log.d(TAG, "load data from network.")
            newslist = NetworkRequest.geNewList()
            // 保存到数据库
            saveNews(news = newslist)
        }

        if (newslist.code == CODE) {
            Result.success(newslist)
        } else {
            Result.failure(RuntimeException("getNews response code is ${newslist.code} msg is ${newslist.msg}"))
        }
    }

    /**
     * 保存到本地数据库
     */
    private suspend fun saveNews(news: XxNewBean) {
        Log.d(TAG, "saveNews: 保存到本地数据库")
        XxEasyDataStore.putData(REQUEST_TIMESTAMP, XxEasyDate.getMillisNextEarlyMorning())
        XxApplication.db.newsItemDao().delete()
        XxApplication.db.newsItemDao().insertAll(news.newslist)
    }

    /**
     * 从本地数据库中加载
     */
    private suspend fun getLocalForNews() =
        XxNewBean(SUCCESS, CODE, XxApplication.db.newsItemDao().getAll())
}