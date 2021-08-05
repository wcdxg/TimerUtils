package com.yuaihen.timerutil

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Administrator.
 * on 2021/07/30
 * Worker
 */
class MyWorker(private val context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        //打开需要启动的APP 通过包名
        val packageName = MainActivity.APP_PACKAGE_NAME
        Log.d("zhj", "doWork: 开始执行定时任务打开 $packageName")
        val intent =
            context.packageManager.getLaunchIntentForPackage(packageName)

        if (!isAppRunningForeground(context)) {
            moveAppToFront(context)
        }
        wakeUp(context)
        intent?.let {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(it)
        }

        return Result.success()
    }

    @SuppressLint("InvalidWakeLockTag")
    private fun wakeUp(context: Context) {
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val kl = km.newKeyguardLock("unLock")
        kl.disableKeyguard()
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val wl = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
            "wakeupScreen"
        )
        wl.acquire()
        wl.release()
    }


    private fun isAppRunningForeground(context: Context): Boolean {
        val activityManager =
            context.applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessList = activityManager.runningAppProcesses ?: return false
        runningAppProcessList.forEach {
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
    }


}