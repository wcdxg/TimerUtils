package com.yuaihen.timerutil

import android.app.Activity
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Administrator.
 * on 2021/07/30
 */
class AppListActivity : Activity() {

    private val appList = mutableListOf<AppInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)

        getPackages()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = AppListAdapter(appList)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : AppListAdapter.OnItemClickListener {
            override fun onClickItem(position: Int) {
                val appInfo = appList[position]
                intent.apply {
                    putExtra("appName", appInfo.appName)
                    putExtra("appPackageName", appInfo.packageName)
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        })
    }

    private fun getPackages() {
        // 获取已经安装的所有应用, PackageInfo　系统类，包含应用信息
        val pm = packageManager
        val packages = pm.getInstalledPackages(0)
        for (i in packages.indices) {
            val packageInfo = packages[i]
            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) { //非系统应用
                // AppInfo 自定义类，包含应用信息
                Log.d(
                    "zhj", "getPackages: "
                            + packageInfo.packageName
                            + " _" + packageInfo.applicationInfo.loadLabel(pm)
                            + " _" + packageInfo.applicationInfo.loadIcon(pm)
                            + " _" + packageInfo.versionName
                )
                appList.add(
                    AppInfo(
                        packageInfo.packageName,
                        packageInfo.applicationInfo.loadLabel(pm) as String,
                        packageInfo.applicationInfo.loadIcon(pm)
                    )
                )
            }
//            else { // 系统应用
//
//            }
        }
    }
}