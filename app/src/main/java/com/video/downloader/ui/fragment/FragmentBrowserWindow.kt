package com.video.downloader.ui.fragment

import android.annotation.*
import android.app.*
import android.content.*
import android.graphics.*
import android.net.*
import android.os.*
import android.webkit.*
import android.webkit.CookieManager
import androidx.activity.*
import androidx.appcompat.app.*
import androidx.constraintlayout.widget.*
import androidx.core.widget.*
import androidx.lifecycle.*
import com.bumptech.glide.*
import com.bumptech.glide.load.resource.drawable.*
import com.bumptech.glide.request.*
import com.google.android.material.button.*
import com.google.android.material.imageview.*
import com.google.android.material.progressindicator.*
import com.google.android.material.snackbar.*
import com.google.android.material.textview.*
import com.mugames.vidsnapkit.dataholders.*
import com.mugames.vidsnapkit.extractor.*
import com.video.downloader.R
import com.video.downloader.base.*
import com.video.downloader.databinding.*
import com.video.downloader.extensions.*
import com.video.downloader.extensions.DownloadListener
import com.video.downloader.ui.*
import kotlinx.coroutines.*
import java.lang.Runnable
import java.net.*

private const val TAG = "FragmentBrowserWindow"

class FragmentBrowserWindow : BaseFragment<FragmentBrowserWindowBinding>(FragmentBrowserWindowBinding::inflate) {

    private var doubleBackToExitPressedOnce = false
    private var pageUrl: String? = ""

    override fun create() {
        arguments?.let {
            pageUrl = it.getString("url")
        }
    }

    override fun initView() {}

    override fun initListeners() {
        binding?.apply {
            editSearch.doOnTextChanged { text, start, before, count ->
                if (text?.isNotBlank() == true) {
                    buttonSearch.enable()
                } else {
                    buttonSearch.disable()
                }
            }
            editSearch.onSearch {
                activity?.hideKeyboard(editSearch)
                val text = editSearch.text.toString()
                if (text.contains("https:") || text.contains("http:")) {
                    replaceUrl(newUrl = text)
                } else {
                    replaceUrl(newUrl = "https://www.google.com/search?q=$text")
                }
            }
            buttonSearch.setOnClickListener {
                activity?.hideKeyboard(buttonSearch)
                val text = editSearch.text.toString()
                if (text.contains("https:") || text.contains("http:")) {
                    replaceUrl(newUrl = text)
                } else {
                    replaceUrl(newUrl = "https://www.google.com/search?q=$text")
                }
            }
            buttonHome.setOnClickListener {
                activity?.hideKeyboard(buttonHome)
                replaceUrl(newUrl = HOME_URL)
            }
            buttonTabs.setOnClickListener {
                activity?.hideKeyboard(buttonTabs)
                (activity as HomeActivity).showTabs()
            }

            editSearchHome.doOnTextChanged { text, start, before, count ->
                if (text?.isNotBlank() == true) {
                    buttonSearchHome.enable()
                } else {
                    buttonSearchHome.disable()
                }
            }
            editSearchHome.onSearch {
                activity?.hideKeyboard(editSearchHome)
                val text = editSearchHome.text.toString()
                if (((activity as HomeActivity).getTabCount() ?: 0) > 1) {
                    if (text.contains("https:") || text.contains("http:")) {
                        replaceUrl(newUrl = text)
                    } else {
                        replaceUrl(newUrl = "https://www.google.com/search?q=$text")
                    }
                } else {
                    if (text.contains("https:") || text.contains("http:")) {
                        (activity as HomeActivity).newTab(newUrl = text)
                    } else {
                        (activity as HomeActivity).newTab(newUrl = "https://www.google.com/search?q=$text")
                    }
                }
            }
            buttonSearchHome.setOnClickListener {
                activity?.hideKeyboard(buttonSearchHome)
                val text = editSearchHome.text.toString()
                if (((activity as HomeActivity).getTabCount() ?: 0) > 1) {
                    if (text.contains("https:") || text.contains("http:")) {
                        replaceUrl(newUrl = text)
                    } else {
                        replaceUrl(newUrl = "https://www.google.com/search?q=$text")
                    }
                } else {
                    if (text.contains("https:") || text.contains("http:")) {
                        (activity as HomeActivity).newTab(newUrl = text)
                    } else {
                        (activity as HomeActivity).newTab(newUrl = "https://www.google.com/search?q=$text")
                    }
                }
            }
            buttonTabsHome.setOnClickListener {
                activity?.hideKeyboard(buttonTabsHome)
                (activity as HomeActivity).showTabs()
            }

            tabFacebook.setOnClickListener {
                activity?.hideKeyboard(tabFacebook)
                val text = "https://m.facebook.com/watch/"
                if (((activity as HomeActivity).getTabCount() ?: 0) > 1) {
                    if (text.contains("https:") || text.contains("http:")) {
                        replaceUrl(newUrl = text)
                    } else {
                        replaceUrl(newUrl = "https://www.google.com/search?q=$text")
                    }
                } else {
                    if (text.contains("https:") || text.contains("http:")) {
                        (activity as HomeActivity).newTab(newUrl = text)
                    } else {
                        (activity as HomeActivity).newTab(newUrl = "https://www.google.com/search?q=$text")
                    }
                }
            }

            tabInstagram.setOnClickListener {
                activity?.hideKeyboard(tabInstagram)
                val text = "https://www.instagram.com/"
                if (((activity as HomeActivity).getTabCount() ?: 0) > 1) {
                    if (text.contains("https:") || text.contains("http:")) {
                        replaceUrl(newUrl = text)
                    } else {
                        replaceUrl(newUrl = "https://www.google.com/search?q=$text")
                    }
                } else {
                    if (text.contains("https:") || text.contains("http:")) {
                        (activity as HomeActivity).newTab(newUrl = text)
                    } else {
                        (activity as HomeActivity).newTab(newUrl = "https://www.google.com/search?q=$text")
                    }
                }
            }
        }
    }

    override fun viewCreated() {
        loadPage()
    }

    override fun onResume() {
        super.onResume()
        binding?.apply {
            textTabCount.text = (activity as HomeActivity).getTabCount().toString()
            textTabCountHome.text = (activity as HomeActivity).getTabCount().toString()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPage() {
        activity?.apply context@{
            binding?.apply {
                webView.apply {
                    settings.apply {
                        javaScriptEnabled = true
                        allowFileAccess = true
                        databaseEnabled = true
                        domStorageEnabled = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                    }
                    CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)
                    addJavascriptInterface(MyJavaScriptInterface(this@context as AppCompatActivity), "android")
                    webView.webChromeClient = object : WebChromeClient() {
                        override fun onProgressChanged(view: WebView?, newProgress: Int) {
                            super.onProgressChanged(view, newProgress)
                            progressLoading.progress = newProgress
                            if (newProgress == 100) {
                                progressLoading.beGone()
                            }
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                            val url = request.url.toString()
                            return !isValidUrl(url)
                        }

                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            progressLoading.progress = 0
                            progressLoading.beVisible()
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            pageUrl = url
                            progressLoading.beGone()
                            editSearch.setText("$pageUrl")
                            if (pageUrl == HOME_URL) {
                                layoutHomePage.beVisible()
                                layoutTabPage.beGone()
                            } else {
                                layoutHomePage.beGone()
                                layoutTabPage.beVisible()
                            }

                        }

                        override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                            super.onReceivedHttpError(view, request, errorResponse)
                        }

                        override fun onLoadResource(view: WebView?, url: String?) {
                            super.onLoadResource(view, url)
                            if (pageUrl?.contains("facebook") == true) {
                                val js = assets.open("instagramScript.js").bufferedReader().use { it.readText() }
                                evaluateJavascript(js, null)
                            } else if (pageUrl?.contains("instagram") == true) {
                                val js = assets.open("official/instagram.js").bufferedReader().use { it.readText() }
                                evaluateJavascript(js, null)
                            } else {
                                val js = assets.open("testScript.js").bufferedReader().use { it.readText() }
                                evaluateJavascript(js, null)
                            }
                        }
                    }

                    replaceUrl("$pageUrl")
                }
            }
        }
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: MalformedURLException) {
            false
        }
    }

    private fun showErrorMessage() {
        binding?.apply {
            layoutError.beVisible()
            webView.beGone()
            buttonReload.setOnClickListener {
                webView.reload()
            }
        }
    }

    private fun hideErrorMessage() {
        binding?.apply {
            layoutError.beGone()
            webView.beVisible()
        }
    }

    fun replaceUrl(newUrl: String?) {
        pageUrl = newUrl
        binding?.apply {
            webView.loadUrl("$pageUrl")
            editSearch.setText("$pageUrl")
            if (pageUrl == HOME_URL) {
                layoutHomePage.beVisible()
                layoutTabPage.beGone()
            } else {
                layoutHomePage.beGone()
                layoutTabPage.beVisible()
            }
        }
    }

    class MyJavaScriptInterface(private val context: AppCompatActivity) {

        @JavascriptInterface
        fun foundVideo(url: String, index: Int) {
            TAG.log("foundVideo===> $url")
        }

        @JavascriptInterface
        fun downloadVideo(url: String, index: Int) {
            context.runOnUiThread {
                context.showDownloadDialog(url + "?__a=1", object : DownloadListener {
                    override fun onDownload(title: String, url: String) {
                        super.onDownload(title, url)
                        val request = DownloadManager.Request(Uri.parse(url)).setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE).setAllowedOverRoaming(false).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title")
                        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                        downloadManager.enqueue(request)
                    }
                })
            }
        }

        @JavascriptInterface
        fun shareVideoLink(url: String) {
            TAG.log("===> $url")
        }

        fun getCookie(name: String): String? {
            val cookies = CookieManager.getInstance().getCookie("https://www.instagram.com/")
            if (cookies == null || !cookies.contains("${name}=")) {
                return null
            }
            return cookies.split("${name}=")[1].split(";")[0]
        }

        @SuppressLint("SetTextI18n")
        @JavascriptInterface
        fun downloadInstagramVideo(url: String) {
            TAG.log("===> $url")
            val postUrl = url
            context.apply {
                lifecycleScope.launch {
                    val cookie = CookieManager.getInstance().getCookie("https://www.instagram.com/")
                    val dialog = context.showProgressDialog()
                    val dialogTitle = dialog.findViewById<MaterialTextView>(R.id.dialog_title)
                    val progressBar = dialog.findViewById<CircularProgressIndicator>(R.id.progress_bar)
                    val layoutProgress = dialog.findViewById<ConstraintLayout>(R.id.layout_progress)
                    val layoutDownload = dialog.findViewById<ConstraintLayout>(R.id.layout_download)
                    val buttonDownload = dialog.findViewById<MaterialButton>(R.id.button_download)
                    val buttonClose = dialog.findViewById<MaterialButton>(R.id.button_close)
                    val imageThumb = dialog.findViewById<ShapeableImageView>(R.id.image_thumb)
                    val textMediaName = dialog.findViewById<MaterialTextView>(R.id.text_media_name)
                    val textMediaDetails = dialog.findViewById<MaterialTextView>(R.id.text_media_details)
                    context.runOnUiThread {
                        layoutProgress?.beVisible()
                        layoutDownload?.beGone()
                        progressBar?.isIndeterminate = true
                        dialogTitle?.text = "Preparing.."
                        if (!context.isFinishing || !context.isDestroyed) {
                            dialog.show()
                        }
                    }
                    val extractor = Extractor.findExtractor(postUrl)
                    extractor?.apply {
                        cookies = cookie
                        start { result ->
                            when (result) {
                                is Result.Success -> {
                                    var fileTitle = ""
                                    var fileType = ""
                                    var fileSize = 0L
                                    var fileUrl = ""
                                    var fileThumb = ""
                                    val formats = result.formats.toMutableList()
                                    if (formats.isNotEmpty()) {
                                        val format = formats[0]
                                        fileTitle = format.title
                                        if (format.videoData.isNotEmpty()) {
                                            val videoData = format.videoData.get(0)
                                            fileType = videoData.mimeType
                                            fileSize = videoData.size
                                            fileUrl = videoData.url
                                            if (format.imageData.isNotEmpty()) {
                                                val imageData = format.imageData.get(0)
                                                fileThumb = imageData.url
                                            }
                                        } else if (format.imageData.isNotEmpty()) {
                                            val imageData = format.imageData.get(0)
                                            fileThumb = imageData.url
                                            fileType = imageData.mimeType
                                            fileSize = imageData.size
                                            fileUrl = imageData.url
                                        }
                                    }
                                    context.runOnUiThread {
                                        layoutProgress?.beGone()
                                        layoutDownload?.beVisible()
                                        if (imageThumb != null) {
                                            Glide.with(context.applicationContext).load(fileThumb).transition(DrawableTransitionOptions.withCrossFade()).apply(RequestOptions().dontTransform().dontAnimate().skipMemoryCache(false)).into(imageThumb)

                                        }
                                        textMediaName?.text = fileTitle
                                        textMediaDetails?.text = "${fileSize.bytesToHumanReadableSize()} \u2022 $fileType"
                                        buttonClose?.setOnClickListener {
                                            Handler(Looper.getMainLooper()).post {
                                                dialog.dismiss()
                                            }
                                        }
                                        buttonDownload?.setOnClickListener {
                                            dialog.dismiss()
                                            val request = DownloadManager.Request(Uri.parse(fileUrl)).setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE).setAllowedOverRoaming(false).setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE or DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED).setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}.mp4")
                                            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                                            downloadManager.enqueue(request)
                                        }
                                    }
                                }

                                is Result.Failed -> {
                                    TAG.log("onClick: ${result.error}")
                                    when (val error = result.error) {
                                        is Error.LoginRequired -> {
                                            TAG.log("LoginRequired: ${error.message}")
                                        }

                                        is Error.InternalError -> {
                                            TAG.log("InternalError: ${error.message}")
                                        }

                                        is Error.NonFatalError -> {
                                            TAG.log("NonFatalError: ${error.message}")
                                        }

                                        is Error.InvalidUrl -> {
                                            TAG.log("InvalidUrl: ${error.message}")
                                        }

                                        is Error.InvalidCookies -> {
                                            TAG.log("InvalidCookies: ${error.message}")
                                        }

                                        is Error.Instagram404Error -> {
                                            TAG.log("Instagram404Error: ${error.isCookiesUsed}")
                                        }

                                        Error.MethodMissingLogic -> {
                                            TAG.log("MethodMissingLogic: ${error.message}")
                                        }
                                    }
                                }

                                is Result.Progress -> {
                                    TAG.log("onProgress: " + result.progressState)
                                }

                                else -> {
                                    TAG.log("onClick: $result")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding?.apply {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        if (((activity as HomeActivity).getTabCount() ?: 0) > 1) {
                            if (doubleBackToExitPressedOnce) {
                                (activity as HomeActivity).closeTab()
                                return
                            }

                            doubleBackToExitPressedOnce = true
                            binding?.apply {
                                Snackbar.make(root, "Tap again to close this tab", Snackbar.LENGTH_SHORT).apply { show() }
                            }

                            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                                doubleBackToExitPressedOnce = false
                            }, 2000)
                        }
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    companion object {

        fun newInstance(url: String?): FragmentBrowserWindow {
            return FragmentBrowserWindow().apply {
                arguments = Bundle().apply {
                    putString("url", url)
                }
            }
        }
    }
}