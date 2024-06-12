package com.video.downloader.ui.pager

import androidx.fragment.app.*
import androidx.viewpager2.adapter.*
import com.video.downloader.ui.fragment.*

class TabPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    val registeredFragments: MutableList<Fragment> = mutableListOf()
    var pages: MutableList<String>? = mutableListOf()

    fun updatePages(newPages: MutableList<String>?) {
        pages = newPages
        notifyItemRangeChanged(0, itemCount)
    }

    fun addPage(newPage: String) {
        pages?.add(newPage)
        notifyItemInserted(pages?.lastIndex ?: 0)
    }

    fun removePage(index: Int) {
        pages?.removeAt(index)
        registeredFragments.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = FragmentBrowserWindow.newInstance(pages?.get(position))
        registeredFragments.add(fragment)
        return fragment
    }

    override fun getItemCount(): Int {
        return pages?.size ?: 0
    }
}