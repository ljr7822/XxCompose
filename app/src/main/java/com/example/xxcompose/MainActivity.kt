package com.example.xxcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import coil.transform.RoundedCornersTransformation
import com.example.xxcompose.bean.NewslistItem
import com.example.xxcompose.utlis.PermissionUtils
import com.example.xxcompose.utlis.ToastDialog
import com.example.xxcompose.vm.MainViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dagger.hilt.android.AndroidEntryPoint

// 在要使用依赖注入的类上方添加@AndroidEntryPoint注解
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PermissionUtils.readAndWrite(this) { ToastDialog.showDialog("授权成功") }
        setContent {
            Column(modifier = Modifier.background(Color.White)) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    InitData()
                }
                XxNavBar()
            }
        }
    }
}

/**
 * 底部导航
 */
@Preview
@Composable
fun XxNavBar() {
    Row(
        modifier = Modifier
            .height(60.dp)
            .padding(16.dp, 0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(R.drawable.ic_home, "首页", MaterialTheme.colors.primary)
        NavItem(R.drawable.ic_chuxing, "出行", Color.Gray)
        NavItem(R.drawable.ic_message, "消息", Color.Gray)
        NavItem(R.drawable.ic_my, "我的", Color.Gray)
    }
}

@Composable
fun RowScope.NavItem(@DrawableRes iconDrawable: Int, desc: String, tint: Color) {
    IconButton(
        onClick = {},
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
    ) {
        Icon(
            painter = painterResource(id = iconDrawable),
            contentDescription = desc,
            modifier = Modifier
                .size(24.dp)
                .weight(1f),
            tint = tint
        )
    }
}


@Composable
private fun XxMainScreen(news: List<NewslistItem>) {
    Scaffold(topBar = {
        // 顶部应用栏
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = { ToastDialog.showDialog("User") }) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "User")
                }
            },
            actions = {
                IconButton(onClick = { ToastDialog.showDialog("Settings") }) {
                    Icon(imageVector = Icons.Filled.Settings, contentDescription = "Settings")
                }
            }
        )
    }) {
        XxBodyContent(news = news)
    }
}

@Composable
fun XxBodyContent(
    news: List<NewslistItem>,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = false),
        onRefresh = { viewModel.getNews(true) })
    {
        LazyColumn(
            // LazyColumn，它只会渲染界面上的可见项，因而有助于提升性能，而且无需使用 scroll 修饰符。
            // Jetpack Compose 中的 LazyColumn 等同于 Android 视图中的 RecyclerView。这里的state就使用rememberLazyListState()。
            state = rememberLazyListState(),
            modifier = modifier.padding(8.dp)
        ) {
            items(news) { new ->
                Row(modifier = Modifier.padding(8.dp)) {
                    AsyncImage(
                        model = new.picUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .width(120.dp)
                            .height(90.dp)
                            .clip(shape = RoundedCornerShape(15)),
                        contentScale = ContentScale.FillBounds
                    )

                    Column(modifier = Modifier.padding(8.dp,0.dp,0.dp,0.dp)) {
                        Text(
                            text = new.title,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp,
                        )
                        Text(text = new.description, fontSize = 12.sp)
                        Row(modifier = Modifier.padding(0.dp, 10.dp)) {
                            Text(text = new.source, fontSize = 12.sp)
                            Text(
                                text = new.ctime,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(8.dp, 0.dp)
                            )
                        }
                    }
                }
                // 分隔线
                Divider(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    color = colorResource(id = R.color.black).copy(alpha = 0.08f)
                )
            }
        }
    }
}

/**
 * 这里就是Compose和ViewModel的组合了
 * 因为：如果不加@Composable注解，它就不是一个可组合函数，就不能这么使用。只要是可组合函数都可以调用viewModel() 函数去获取ViewModel，
 * 而我们这里的就是MainViewModel，viewModel() 会返回一个现有的 ViewModel，或在给定作用域内创建一个新的 ViewModel。
 * 只要该作用域处于有效状态，就会保留 ViewModel。例如，如果在某个 Activity 中使用了可组合项，
 * 则在该 Activity 完成或进程终止之前，viewModel() 会返回同一实例。这里的作用域很重要，因为普通函数如果没有作用域的话是无法调用可组合函数。
 */
@Composable
fun InitData(viewModel: MainViewModel = viewModel()) {

    // 注册为监听器，并将值表示为 State。每当发出一个新值时，Compose 都会重组界面中使用该 state.value 的部分。
    val dataState = viewModel.result.observeAsState()

    viewModel.getNews(false)
    // 通过这个代码会执行网络请求，观察返数据状态，得到一个dataState，
    dataState.value?.let {
        it.getOrNull()?.newslist?.let { XxMainScreen(news = it) }
    }
}