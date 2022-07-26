package com.example.xxcompose.utlis

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.xxcompose.R
import com.example.xxcompose.XxApplication
import com.example.xxcompose.utlis.ThreadUtil.ktxRunOnUi

/**
 * 弹窗工具类
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose.utlis
 */
/**
 * 消息内容相同时，默认的时间间隔
 */
const val DEFAULT_SAME_TEXT_TIME_INTERVAL = 2500

const val TAG = "ToastDialog"

object ToastDialog {

    private var mToast: Toast = Toast(XxApplication.context)
    private var defaultText = ""
    private var previousText = ""
    private var lastTime:Long = 0


    fun showDialog(resId: Int) {
        showDialog(XxApplication.context.getString(resId));
    }

    fun showDialog(text: String) {
        showDialog(text, Toast.LENGTH_SHORT);
    }

    fun showDialog(resId: Int, duration: Int) {
        showDialog(XxApplication.context.getString(resId), duration)
    }

    fun showDialog(text: String, duration: Int) {
        showDialog(text, duration, 0, 0)
    }

    fun showDialog(text: String, duration: Int, x0ff: Int, y0ff: Int) {
        if (TextUtils.isEmpty(text)) {
            return
        }
        val realText: String = changeTechString(text)
        ThreadUtil.ktxRunOnUi {
            try {
                if (isSameText(realText)){
                    return@ktxRunOnUi
                }
                createToast(XxApplication.context,realText,duration)
            }catch (e:Exception){
                Log.e(TAG,e.toString())
            }
        }
    }

    fun showDialog(context: Context, text: String, duration: Int){
        showDialog(context, text, duration,0,0)
    }

    fun showDialog(context: Context,text: String,duration: Int,x0ff: Int, y0ff: Int){
        if (TextUtils.isEmpty(text)) {
            return;
        }
        val realText: String = changeTechString(text)
        ThreadUtil.ktxRunOnUi {
            try {
                //跟之前的显示相同，则判断是否超过时间限制才显示
                if (isSameText(realText)) {
                    return@ktxRunOnUi
                }

                createToast(XxApplication.context, realText, duration);
            } catch (e:Exception) {
            }
        }
    }

    fun createToast(context: Context,text: String,duration: Int){
        //移除之前的Toast
        if (mToast != null) {
            mToast.cancel();
        }

        var contentView: View = LayoutInflater.from(context).inflate(R.layout.toast_text_black,null)
        var toastTv: TextView = contentView.findViewById(R.id.toast_text)
        toastTv.text = text

        mToast = Toast.makeText(context,defaultText,duration)
        mToast.setView(contentView)
        mToast.setGravity(Gravity.CENTER, 0, 0)
        mToast.show()
    }

    fun dismiss(){
        if (mToast != null) {
            mToast.cancel()
        }
    }

    private fun isSameText(text: String):Boolean{
        if(!text.equals(previousText)){
            previousText = text;
            lastTime = System.currentTimeMillis()
            return false;
        }

        val now = System.currentTimeMillis();
        if (now - lastTime < DEFAULT_SAME_TEXT_TIME_INTERVAL) {
            return true;
        }
        lastTime = now;
        return false;
    }

    private fun changeTechString(text: String): String {
        if (text.startsWith("Use JsonReader.set") || text.startsWith("Unable to resolve host")) {
            return "网络错误，请检查网络后重试";
        }

        if (text.startsWith("无效参数")) {
            return "操作失败";
        }
        return text;
    }
}