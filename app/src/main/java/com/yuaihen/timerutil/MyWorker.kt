package com.yuaihen.timerutil

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Administrator.
 * on 2021/07/30
 */
class MyWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        //打开需要启动的APP 通过包名
        Log.d("zhj", "doWork: 开始执行定时任务打开 ${MainActivity.APP_PACKAGE_NAME}")
//        val intent =
//            applicationContext.packageManager.getLaunchIntentForPackage(MainActivity.APP_PACKAGE_NAME)
//        intent?.let {
//            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            applicationContext.startActivity(it)
//        }
//        val utils = NotificationUtils(context)
//        utils.clearAllNotifiication()
//        utils.sendNotificationFullScreen(MainActivity.APP_PACKAGE_NAME)
        val appRunningForeground = isAppRunningForeground(context)
        Log.d("zhj", "doWork appRunningForeground: $appRunningForeground")
//        while (num < 3) {
//            moveAppToFront(context)
//        }
//        num = 0
//        val utils = NotificationUtils(context)
//        utils.clearAllNotifiication()
//        utils.sendNotificationFullScreen(MainActivity.APP_PACKAGE_NAME)
//        val intent =
//            applicationContext.packageManager.getLaunchIntentForPackage(MainActivity.APP_PACKAGE_NAME)
//        intent?.let {
//            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//            applicationContext.startActivity(it)
//        }
        val intent = Intent("action")
        context.sendBroadcast(intent)
        return Result.success()
    }

    private var num = 0

    private fun isAppRunningForeground(context: Context): Boolean {
        val activityManager =
            context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessList = activityManager.runningAppProcesses ?: return false
        Log.d("zhj", "运行中的进程数量为 ${runningAppProcessList.size}")
        runningAppProcessList.forEach {
            Log.d(
                "zhj",
                "应用包名= ${it.processName} 优先级= ${it.importance}"
            )
            if (it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                && it.processName == context.applicationInfo.processName
            ) {
                return true
            }
        }
        return false
    }

    private fun moveAppToFront(context: Context) {
        val activityManager =
            context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningTasks = activityManager.getRunningTasks(100)
        for (taskInfo in runningTasks) {
            if (taskInfo.topActivity!!.packageName == context.packageName) {
                activityManager.moveTaskToFront(taskInfo.id, 0)
                Log.d("zhj", "moveAppToFront: ")
                break
            }
        }

        num++
    }


}