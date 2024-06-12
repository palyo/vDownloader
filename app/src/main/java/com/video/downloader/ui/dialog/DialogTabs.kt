package com.video.downloader.ui.dialog

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.video.downloader.base.BaseDialog
import com.video.downloader.databinding.DialogTabsBinding
import com.video.downloader.ui.adapter.TabsAdapter
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator

class DialogTabs : BaseDialog<DialogTabsBinding>(DialogTabsBinding::inflate) {

    private var tabAdapter: TabsAdapter? = null

    interface TabListener {
        fun onTabOpen(tab: String?, position: Int) {}
        fun onTabClose(tab: String?, position: Int) {}
    }

    companion object {
        var listener: TabListener? = null
        var pages: MutableList<String>? = mutableListOf()
        fun newInstance(pages: MutableList<String>?, listener: TabListener): DialogTabs {
            this.pages = pages
            this.listener = listener
            return DialogTabs().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initData()
    }

    private fun initData() {

    }


    private fun initAdapter() {
        activity?.apply context@{
            binding?.recyclerView?.apply {
                itemAnimator.apply {
                    FadeInUpAnimator(OvershootInterpolator(1f))
                }
                scheduleLayoutAnimation()
                layoutManager = LinearLayoutManager(this@context, RecyclerView.VERTICAL, false)
                tabAdapter = TabsAdapter(object : TabsAdapter.TabListener {
                    override fun onTabOpen(tab: String?, position: Int) {
                        super.onTabOpen(tab, position)
                        listener?.onTabOpen(tab, position)
                        dialog?.dismiss()
                    }

                    override fun onTabClose(tab: String?, position: Int) {
                        super.onTabClose(tab, position)
                        listener?.onTabClose(tab, position)
                        dialog?.dismiss()
                    }
                })
                adapter = tabAdapter
                tabAdapter?.addAll(pages)
            }
        }
    }


    override fun initListeners() {
        binding?.apply {

        }
    }

    override fun initView() {
        binding?.apply {
            toolbar.title = ""
            toolbar.setNavigationOnClickListener {
                dialog?.dismiss()
            }
            root.apply {
                ViewCompat.setOnApplyWindowInsetsListener(this) { _, windowInsets ->
                    val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
                    updatePadding(
                        top = insets.top
                    )
                    WindowInsetsCompat.CONSUMED
                }
            }
        }
    }
}