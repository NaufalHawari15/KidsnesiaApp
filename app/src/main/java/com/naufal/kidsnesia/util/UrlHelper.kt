package com.naufal.kidsnesia.util

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object UrlHelper {
    private const val LOCAL_BASE_URL = "http://localhost:8000"
    private const val NGROK_BASE_URL = "https://921f-36-76-102-13.ngrok-free.app"

    fun fixVideoUrl(originalUrl: String?): String {
        if (originalUrl.isNullOrBlank()) return ""
        return originalUrl.replace(LOCAL_BASE_URL, NGROK_BASE_URL)
    }
}
