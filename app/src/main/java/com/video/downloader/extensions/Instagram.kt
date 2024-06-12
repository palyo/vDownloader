package com.video.downloader.extensions

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Environment
import com.google.gson.Gson
import com.video.downloader.model.Graphql
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup

private const val TAG = "Instagram"

fun fetchPostData(postLink: String): String? {
    val client = OkHttpClient()
    val request = Request.Builder()
        .url(postLink)
        .build()

    val response = client.newCall(request).execute()
    return response.body?.string()
}

fun extractDownloadUrl(instagramPostUrl: String): String? {
    try {
        // Load the Instagram post page using JSoup
        val doc = Jsoup.connect(instagramPostUrl).get()

        // Find the script tag that contains the download URL
        val scriptTag = doc.select("script[type=\"text/javascript\"]").firstOrNull {
            it.html().contains("video_url")
        }

        // Extract the download URL from the script tag
        val pattern = "video_url\":\"(.*?)\"".toRegex()
        val matchResult = pattern.find(scriptTag?.html() ?: "")
        return matchResult?.groupValues?.get(1)
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun getDownloadUrl(postLink: String): String? {
    val postData = fetchPostData(postLink)
    val gson = Gson()
    val graphql = gson.fromJson(postData, Graphql::class.java)
    val shortcodeMedia = graphql.shortcode_media

    // Check if the post is a video or image
    if (shortcodeMedia.is_video) {
        // If the post is a video, check if it is a single video or a carousel of videos
        if (shortcodeMedia.video_url != null) {
            // Single video
            return shortcodeMedia.video_url
        } else {
            // Carousel of videos
            val edgeSidecarToChildren = shortcodeMedia.edge_sidecar_to_children
            if (edgeSidecarToChildren != null) {
                val edges = edgeSidecarToChildren.edges
                if (edges.isNotEmpty()) {
                    return edges[0].node.video_url
                }
            }
        }
    } else {
        // If the post is an image
        return shortcodeMedia.display_url
    }

    return null
}

fun Context.downloadFile(url: String, fileName: String) {
    val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
    val downloadUri = Uri.parse(url)
    val request = DownloadManager.Request(downloadUri)
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        .setAllowedOverRoaming(false)
        .setTitle(fileName)
        .setDescription("Downloading")
        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
    downloadManager.enqueue(request)
}