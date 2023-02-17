package com.example.musicwithyou.data.local.content_resolver

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns
import android.provider.MediaStore

object MediaStoreInternals: BaseColumns {

    val CONTENT_URI: Uri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT)
        .authority(MediaStore.AUTHORITY)
        .appendPath("external")
        .appendEncodedPath("audio/albumart")
        .build()

}