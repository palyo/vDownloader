package com.video.downloader.extensions

import java.util.concurrent.*

fun Long.getReadableSize(): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var unitIndex = 0
    var size = this.toDouble()
    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024.0
        unitIndex++
    }
    return String.format("%.2f %s", size, units[unitIndex])
}

fun Long.getReadableDuration(): String {
    val hours: Long = TimeUnit.MILLISECONDS.toHours(this)
    val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(this) % 60
    val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(this) % 60
    return if (hours > 0) {
        String.format("%02d hr %02d min %02d sec", hours, minutes, seconds)
    } else if (minutes > 0) {
        String.format("%02d min %02d sec", minutes, seconds)
    } else {
        String.format("%02d sec", seconds)
    }
}

var fileSizeUnits = arrayOf("bytes", "KB", "MB", "GB", "TB", "PB")

fun Long.bytesToHumanReadableSize(): String {
    var sizeToReturn = ""
    var bytes = this.toDouble()
    var index = 0
    while (index < fileSizeUnits.size) {
        if (bytes < 1024) {
            break
        }
        bytes /= 1024
        index++
    }
    sizeToReturn = "%.2f".format(bytes) + " " + fileSizeUnits.get(index)
    return sizeToReturn
}