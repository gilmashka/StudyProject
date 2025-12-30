package com.example.studyproject_5.util

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap

fun ImageBitmap.toAndroidBitmap(): android.graphics.Bitmap {
    return this.asAndroidBitmap()
}