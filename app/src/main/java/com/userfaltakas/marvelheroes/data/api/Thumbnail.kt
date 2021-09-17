package com.userfaltakas.marvelheroes.data.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Thumbnail(
    val extension: String? = null,
    val path: String? = null
) : Parcelable {
    fun getURL(): String {
        return "${this.path}.${this.extension}"
    }
}