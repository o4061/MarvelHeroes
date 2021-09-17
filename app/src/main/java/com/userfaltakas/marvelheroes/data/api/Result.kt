package com.userfaltakas.marvelheroes.data.api

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "squad"
)
@Parcelize
data class Result(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String? = null,
    val name: String? = null,
    val thumbnail: Thumbnail? = null,
) : Parcelable