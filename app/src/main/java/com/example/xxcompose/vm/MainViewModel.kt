package com.example.xxcompose.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.xxcompose.repo.NewListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose.vm
 */
// 直接通过注入的方式在构造函数中传入NewListRepository
@HiltViewModel
class MainViewModel @Inject constructor(
    repository: NewListRepository
) : ViewModel() {
    private val isRefresh = MutableLiveData<Boolean>()
    val result = Transformations.switchMap(isRefresh) {
        repository.getNewList(isRefresh = it)
    }

    fun getNews(refresh: Boolean) {
        isRefresh.value = refresh
    }
}