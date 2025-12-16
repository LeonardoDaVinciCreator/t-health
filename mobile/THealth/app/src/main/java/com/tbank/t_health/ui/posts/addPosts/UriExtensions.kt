package com.tbank.t_health.ui.posts.addPosts

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Uri.toBase64(context: Context): String {
    return try {
        context.contentResolver.openInputStream(this)?.use { inputStream ->
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 80, baos)
            val byteArray = baos.toByteArray()
            "data:image/jpeg;base64,${Base64.encodeToString(byteArray, Base64.DEFAULT)}"
        } ?: ""
    } catch (e: Exception) {
        android.util.Log.e("MEDIA", "Error converting to base64", e)
        ""
    }
}
