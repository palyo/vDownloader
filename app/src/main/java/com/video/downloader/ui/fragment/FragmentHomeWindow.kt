package com.video.downloader.ui.fragment

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import com.video.downloader.base.BaseFragment
import com.video.downloader.databinding.FragmentHomeWindowBinding
import com.video.downloader.extensions.disable
import com.video.downloader.extensions.enable
import com.video.downloader.ui.HomeActivity

class FragmentHomeWindow :
    BaseFragment<FragmentHomeWindowBinding>(FragmentHomeWindowBinding::inflate) {

    private var pageUrl: String? = ""

    override fun create() {
        arguments?.let {
            pageUrl = it.getString("url")
        }
    }

    override fun initView() {

    }

    override fun initListeners() {
        binding?.apply {
            editSearch.doOnTextChanged { text, start, before, count ->
                if (text?.isNotBlank() == true) {
                    buttonSearch.enable()
                } else {
                    buttonSearch.disable()
                }
            }

            buttonSearch.setOnClickListener {
                val text = editSearch.text.toString()
                if (text.contains("https:") || text.contains("http:")) {
                    (activity as HomeActivity).newTab(newUrl = text)
                } else {
                    (activity as HomeActivity).newTab(newUrl = "https://www.google.com/search?q=$text")
                }
            }

            buttonTabs.setOnClickListener {
                (activity as HomeActivity).showTabs()
            }
        }
    }

    override fun viewCreated() {
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            textTabCount.text = (activity as HomeActivity).getTabCount().toString()
        }
    }

    companion object {
        fun newInstance(url: String?): FragmentHomeWindow {
            return FragmentHomeWindow().apply {
                arguments = Bundle().apply {
                    putString("url", url)
                }
            }
        }
    }
}