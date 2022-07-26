package com.example.xxcompose.utlis

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * 权限工具类
 *
 * @Author: xiaoxun
 * @CreateDate: 2022/7/20
 * @Package: com.example.xxcompose
 */
object PermissionUtils {
    private const val RESULT_CODE_TAKE_CAMERA = 7461    // 拍照
    private const val RESULT_CODE_OPEN_ALBUM = 7462     // 读写
    private const val RESULT_CODE_SOUND_RECORD = 7463   // 录音

    private var cameraCallback: (() -> Unit)? = null        // 相机回调
    private var readAndWriteCallback: (() -> Unit)? = null  // 读写回调
    private var audioCallback: (() -> Unit)? = null         // 录音回调

    /**
     * 相机权限申请
     */
    fun camera(context: Context, cameraCallback: () -> Unit) {
        PermissionUtils.cameraCallback = cameraCallback
        permission(context, Manifest.permission.CAMERA, RESULT_CODE_TAKE_CAMERA, cameraCallback)
    }

    /**
     * 读写权限申请
     */
    fun readAndWrite(context: Context, readAndWriteCallback: () -> Unit) {
        PermissionUtils.readAndWriteCallback = readAndWriteCallback
        permission(
            context,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            RESULT_CODE_OPEN_ALBUM,
            readAndWriteCallback
        )
    }

    /**
     * 录音权限申请
     */
    fun audio(context: Context, audioCallback: () -> Unit) {
        PermissionUtils.audioCallback = audioCallback
        permission(
            context,
            Manifest.permission.RECORD_AUDIO,
            RESULT_CODE_SOUND_RECORD,
            audioCallback
        )
    }

    /**
     * 权限申请结果
     */
    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
        when (requestCode) {
            RESULT_CODE_TAKE_CAMERA -> {    // 拍照
                if (cameraAccepted) {
                    cameraCallback?.let { it() }
                } else {
                    // 用户拒绝
                    ToastDialog.showDialog("请开启应用拍照权限")
                }
            }
            RESULT_CODE_OPEN_ALBUM -> { // 读写
                if (cameraAccepted) {
                    readAndWriteCallback?.let { it() }
                } else {
                    ToastDialog.showDialog("请开启应用读取权限")
                }
            }
            RESULT_CODE_SOUND_RECORD -> { // 录音
                if (cameraAccepted) {
                    audioCallback?.let { it() }
                } else {
                    ToastDialog.showDialog("请开启应用录音权限")
                }
            }
        }
    }

    /**
     * 权限申请
     */
    private fun permission(
        context: Context,
        systemCode: String,
        resultCode: Int,
        callback: () -> Unit
    ) {
        // 判断是否有权限
        if (ContextCompat.checkSelfPermission(
                context,
                systemCode
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            callback()
        } else {
            // 没有就申请权限
            ActivityCompat.requestPermissions(context as Activity, arrayOf(systemCode), resultCode)
        }
    }
}