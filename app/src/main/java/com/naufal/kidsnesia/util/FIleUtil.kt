package com.naufal.kidsnesia.util

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileOutputStream

object FileUtil {

    fun from(context: Context, uri: Uri): File {
        val extension = getExtension(context, uri)
        if (extension !in listOf("jpg", "jpeg", "png")) {
            throw IllegalArgumentException("File harus berupa JPG, JPEG, atau PNG")
        }

        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "upload_${System.currentTimeMillis()}.$extension"
        val file = File(context.cacheDir, fileName)

        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        outputStream.close()
        inputStream?.close()

        return file
    }

    private fun getExtension(context: Context, uri: Uri): String {
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType ?: "") ?: "jpg"
    }
}

