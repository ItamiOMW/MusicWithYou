package com.example.musicwithyou.utils

import kotlin.math.floor


const val SECONDS_IN_MINUTE = 60

fun Long.timestampToDuration(): String {
    val totalSeconds = floor(this / 1E3).toInt()
    val minutes = totalSeconds / SECONDS_IN_MINUTE
    val remainingSeconds = totalSeconds - (minutes * SECONDS_IN_MINUTE)

    return if (this < 0) "--:--"
    else "%d:%02d".format(minutes,remainingSeconds)
}

fun Long.timestampToSeconds(): Long {
    return floor(this / 1E3).toLong()
}