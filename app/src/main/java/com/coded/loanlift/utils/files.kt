package com.coded.loanlift.utils

import android.content.Context
import android.net.Uri
import java.io.File

object FileUtils {
    fun getFileFromUri(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "upload_${System.currentTimeMillis()}")
        file.outputStream().use { output -> inputStream.copyTo(output) }
        return file
    }
}