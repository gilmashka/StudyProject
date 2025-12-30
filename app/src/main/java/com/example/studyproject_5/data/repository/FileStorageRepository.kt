package com.example.studyproject_5.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class FileStorageRepository(
    private val context: Context
) {
    private val imagesDir by lazy {
        File(context.filesDir, "posts_images").apply {
            if (!exists()) {
                mkdirs()
            }
        }
    }

    fun saveImage(bitmap: Bitmap): String {
        val fileName = "${UUID.randomUUID()}.jpg"
        val file = File(imagesDir, fileName)

        FileOutputStream(file).use { stream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        }

        return file.absolutePath
    }

    fun getImageUri(path: String): Uri {
        return Uri.fromFile(File(path))
    }

    fun deleteImage(path: String) {
        File(path).delete()
    }

    fun bitmapFromUri(uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }
        } catch (e: Exception) {
            null
        }
    }
}