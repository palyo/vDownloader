package com.video.downloader.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.video.downloader.databinding.LayoutRowItemTabBinding
import com.video.downloader.extensions.*

class TabsAdapter(
    private var listeners: TabListener?,
) : RecyclerView.Adapter<TabsAdapter.MyViewHolder>() {
    interface TabListener {
        fun onTabOpen(tab: String?, position: Int) {}
        fun onTabClose(tab: String?, position: Int) {}
    }

    private var tabs: MutableList<String>? = ArrayList()

    fun addAll(tabs: MutableList<String>?) {
        this.tabs = tabs
        notifyItemRangeChanged(0, itemCount)
    }

    override fun getItemCount(): Int = tabs?.count() ?: 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutRowItemTabBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class MyViewHolder(var binding: LayoutRowItemTabBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = tabs?.get(position)
        holder.binding.apply {
            if (item == HOME_URL) {
                titleTab.text = HOME_TAB
            } else {
                titleTab.text = item.toString()
            }
            closeTab.setOnClickListener {
                listeners?.onTabClose(item, position)
            }
            root.setOnClickListener {
                listeners?.onTabOpen(item, position)
            }
        }
    }
}