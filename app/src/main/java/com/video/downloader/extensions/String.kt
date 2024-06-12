package com.video.downloader.extensions

fun String.removeLastPartFromUrl(): String {
    val parts = split("/")
    return if (parts.size >= 2) parts.subList(0, parts.size - 1).joinToString("/") else this
}