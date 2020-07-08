package com.deepak.nasa.utils.common

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import java.util.*
import java.util.regex.Pattern


@Throws(Throwable::class)
fun getVideoFrame(videoPath: String?): Bitmap? {
    val bitmap: Bitmap?
    var mediaMetadataRetriever: MediaMetadataRetriever? = null
    try {
        mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(videoPath, HashMap())
        bitmap = mediaMetadataRetriever.getFrameAtTime(3000000, MediaMetadataRetriever.OPTION_PREVIOUS_SYNC)
    } catch (e: Exception) {
        e.printStackTrace()
        throw Throwable("Exception in retrieve video frame:$e")
    } finally {
        mediaMetadataRetriever?.release()
    }
    return bitmap
}

fun getYoutubeVideoId(inUrl: String): String? {
    Pattern.compile("(?<=watch\\?v=|/videos/|embed/)[^#&?]*").matcher(inUrl).apply {
        return if (this.find()) this.group()  else "Id not retrievable"
    }

}



