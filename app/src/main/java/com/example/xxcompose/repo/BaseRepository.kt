package com.example.xxcompose.repo

import androidx.lifecycle.liveData
import kotlin.coroutines.CoroutineContext

/**
 * Repository基础类
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose.repo
 */
open class BaseRepository {

    /**
     * 在fire()函数的内部会先调用一下liveData()函数，
     * 然后在liveData()函数的代码块中统一进行try catch处理，并在try语句中调用传入的Lambda表达式中的代码，
     * 最终Lambda表达式的执行结果并调用 emit() 方法发射出去。
     */
    fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            // 通知数据变化
            emit(result)
        }
}