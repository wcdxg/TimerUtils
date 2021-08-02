package com.yuaihen.timerutil

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

/**
 * Created by Administrator.
 * on 2021/07/30
 */
class MyWorker(val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        //打开需要启动的APP 通过包名
        Log.d("zhj", "doWork: 开始执行定时任务")
        Log.d("zhj", "doWork: ${MainActivity.APP_PACKAGE_NAME}")
        val intent =
            applicationContext.packageManager.getLaunchIntentForPackage(MainActivity.APP_PACKAGE_NAME)
        intent?.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            applicationContext.startActivity(it)
        }

        return Result.success()
    }


}