package com.video.downloader.ui

import android.os.*
import androidx.activity.*
import androidx.viewpager2.widget.*
import com.video.downloader.base.*
import com.video.downloader.databinding.*
import com.video.downloader.extensions.*
import com.video.downloader.ui.dialog.*
import com.video.downloader.ui.pager.*

class HomeActivity : BaseDarkActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate, isPadding = true) {

    private var pager: TabPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPager()
    }

    private fun initPager() {
        pager = TabPagerAdapter(this@HomeActivity)
        binding?.apply {
            viewPager.isUserInputEnabled = false
            viewPager.adapter = pager
            pager?.addPage(HOME_URL)
        }
    }

    fun newTab(newUrl: String?) {
        pager?.addPage("$newUrl")
        binding?.apply {
            if ((pager?.pages?.count() ?: 0) > 1) {
                viewPager.currentItem = pager?.pages?.lastIndex ?: 0
            }
        }
    }

    fun closeTab() {
        binding?.apply {
            pager?.removePage(viewPager.currentItem)
        }
    }

    fun showTabs() {
        val dialog = DialogTabs.newInstance(pager?.pages, object : DialogTabs.TabListener {
            override fun onTabOpen(tab: String?, position: Int) {
                super.onTabOpen(tab, position)
                binding?.apply {
                    viewPager.currentItem = position
                }
            }

            override fun onTabClose(tab: String?, position: Int) {
                super.onTabClose(tab, position)
                binding?.apply {
                    if ((pager?.pages?.count() ?: 0) > 1) {
                        pager?.removePage(position)
                    }
                }
            }
        })
        dialog.isCancelable = true
        val ft = supportFragmentManager.beginTransaction()
        dialog.show(ft, "DialogTabs")
    }

    fun getTabCount(): Int? {
        return pager?.pages?.count()
    }

    override fun initListeners() {
        binding?.apply {

        }
    }

    private val pagerListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding?.apply {
                val fragments = pager?.registeredFragments
            }
        }
    }

    override fun initView() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }
        })
    }
}