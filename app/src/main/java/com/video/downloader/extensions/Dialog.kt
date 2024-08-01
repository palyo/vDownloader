package com.video.downloader.extensions

import android.app.*
import android.content.res.*
import android.graphics.*
import android.graphics.drawable.*
import android.media.*
import android.net.*
import android.view.*
import androidx.core.content.*
import com.bumptech.glide.*
import com.bumptech.glide.load.resource.drawable.*
import com.google.android.material.bottomsheet.*
import com.video.downloader.R
import com.video.downloader.databinding.*
import kotlinx.coroutines.*
import java.io.*
import java.net.*

interface DownloadListener {

    fun onDownload(title: String, url: String) {}
}

fun Activity.showDownloadDialog(url: String, listener: DownloadListener?) {
    val dialog = BottomSheetDialog(this, R.style.Theme_VidDownloader_BottomSheetDialogTheme)
    val bind: LayoutDialogVideoDownloadBinding = LayoutDialogVideoDownloadBinding.inflate(layoutInflater)
    bind.apply {
        dialog.setContentView(root)
        val videoUri = Uri.parse(url)
        val videoName = videoUri.lastPathSegment
        textMediaName.text = videoName
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(url, HashMap())
            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
            textMediaDuration.text = "${duration.getReadableDuration()}"
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            retriever.release()
        }
        CoroutineScope(Dispatchers.IO).launch {
            var size = 0L
            try {
                val uri = URL(url)
                val connection = uri.openConnection() as HttpURLConnection
                connection.requestMethod = "HEAD"
                connection.connect()
                size = connection.contentLength.toLong()
                launch(Dispatchers.Main) { textMediaSize.text = "${size.getReadableSize()}" }
                connection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        Glide.with(root.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .dontAnimate()
            .into(imageThumb)
        buttonDownload.setOnClickListener {
            dialog.dismiss()
            listener?.onDownload(title = textMediaName.text.toString(), url = url)
        }
        (root.parent as View).backgroundTintMode = PorterDuff.Mode.CLEAR
        (root.parent as View).backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        (root.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    val params = dialog.window?.attributes
    params?.width = WindowManager.LayoutParams.MATCH_PARENT
    params?.height = WindowManager.LayoutParams.WRAP_CONTENT
    params?.gravity = Gravity.CENTER
    dialog.window?.attributes = params
    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    dialog.setCanceledOnTouchOutside(true)
    dialog.window?.setDimAmount(.34f)
    dialog.window?.navigationBarColor =
        ContextCompat.getColor(this@showDownloadDialog, R.color.colorBlack)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    if (!isFinishing || !isDestroyed)
        dialog.show()
}

fun Activity.showProgressDialog(): BottomSheetDialog {
    val dialog = BottomSheetDialog(this, R.style.Theme_VidDownloader_BottomSheetDialogTheme)
    val bindDialog: LayoutDialogSourceLoadDownloadBinding = LayoutDialogSourceLoadDownloadBinding.inflate(layoutInflater)
    dialog.setContentView(bindDialog.root)
    dialog.setCancelable(true)

    dialog.window?.apply {
        applyDialogConfig()
    }
    return dialog
}

fun Window.applyDialogConfig() {
    val params = attributes
    params?.width = WindowManager.LayoutParams.MATCH_PARENT
    params?.height = WindowManager.LayoutParams.WRAP_CONTENT
    params?.gravity = Gravity.CENTER
    attributes = params
    setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

    setDimAmount(.50f)
    setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}