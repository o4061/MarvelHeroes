package com.userfaltakas.marvelheroes.data.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val count: Int? = null,
    val limit: Int? = null,
    val offset: Int? = null,
    val results: ArrayList<Result>? = null,
    val total: Int? = null
) : Parcelable