package com.yuaihen.timerutil

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.work.*
import java.util.concurrent.TimeUnit
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private var appPackageName: String = ""
    private var appName: String = ""
    private lateinit var tvAppName: TextView
    private lateinit var editHour: AppCompatEditText
    private lateinit var editMinutes: AppCompatEditText
    private lateinit var progressBar: ProgressBar
    private var isSelectApp = false

    companion object {
        var APP_PACKAGE_NAME: String = ""
    }

    private var myReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_main)

        tvAppName = findViewById(R.id.tv_appName)
        progressBar = findViewById(R.id.progressBar)
        editHour = findViewById(R.id.edit_hour)
        editMinutes = findViewById(R.id.edit_minutes)

        findViewById<Button>(R.id.btn_selectApp).setOnClickListener {
            progressBar.visibility = View.VISIBLE
            startActivityForResult(Intent(this, AppListActivity::class.java), 999)
        }
        findViewById<Button>(R.id.btn_start).setOnClickListener {
            initWorker()
        }

    }

    private fun initWorker() {
        hideSoftInput()
        if (!isSelectApp) {
            Toast.makeText(this, "????????????????????????APP", Toast.LENGTH_SHORT).show()
            return
        }

        val hourStr = editHour.text.toString().trim()
        val minutesStr = editMinutes.text.toString().trim()
        if (hourStr.isEmpty() && minutesStr.isEmpty()) {
            return
        }

        var hour = 0
        var min = 0
        if (hourStr.isNotEmpty()) {
            hour = hourStr.toInt()
        }
        if (minutesStr.isNotEmpty()) {
            min = minutesStr.toInt()
        }

        if (hour == 0 && min == 0) {
            Toast.makeText(this, "???????????????0", Toast.LENGTH_SHORT).show()
            return
        }

        //??????
        val minutes = hour * 60 + min
        if (minutes <= 0) return
//        val constraints = Constraints.Builder()
//            .setRequiredNetworkType(NetworkType.CONNECTED)
//            .build()

        val myWorker = OneTimeWorkRequestBuilder<MyWorker>()
            .setInitialDelay(minutes.toLong(), TimeUnit.MINUTES)
            .build()
//

        WorkManager.getInstance(this)
            .beginWith(myWorker)
            .enqueue()

        Toast.makeText(this, "????????????,????????????", Toast.LENGTH_SHORT).show()
        setBrightness()
    }

    private fun hideSoftInput() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            this.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    /**
     * ????????????Activity??????????????????
     * ?????????????????????????????????255???????????????????????????
     * screenBrightness ??????????????????[0,1]??????
     */
    private fun setBrightness() {
        val lp = window.attributes
        lp.screenBrightness = 0f
        window.attributes = lp
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 999) {
            if (resultCode == RESULT_OK) {
                appPackageName = data?.getStringExtra("appPackageName") ?: ""
                APP_PACKAGE_NAME = appPackageName
                appName = data?.getStringExtra("appName") ?: ""
                tvAppName.text = appName
                isSelectApp = true
            }
        }
    }

    override fun onRestart() {
        super.onRestart()
        progressBar.visibility = View.INVISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(myReceiver)
    }

}