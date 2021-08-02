package com.yuaihen.timerutil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Administrator.
 * on 2021/07/30
 */
class AppListAdapter(private val list: List<AppInfo>) :
    RecyclerView.Adapter<AppListAdapter.AppListHolder>() {

    inner class AppListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvAppName: TextView = itemView.findViewById(R.id.tv_appName)
        var tvAppPackageName: TextView = itemView.findViewById(R.id.tv_appPackageName)
        var ivAppIcon: ImageView = itemView.findViewById(R.id.iv_appIcon)
        var cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_app_info, parent, false)
        return AppListHolder(itemView)
    }

    override fun onBindViewHolder(holder: AppListHolder, position: Int) {
        holder.tvAppName.text = list[position].appName
        holder.tvAppPackageName.text = list[position].packageName
        holder.ivAppIcon.setImageDrawable(list[position].appIcon)

        holder.cardView.setOnClickListener {
            mOnItemClickListener?.onClickItem(position)
        }
    }


    interface OnItemClickListener {
        fun onClickItem(position: Int)
    }

    private var mOnItemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return list.size
    }
}