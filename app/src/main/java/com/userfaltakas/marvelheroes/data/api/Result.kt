package com.userfaltakas.marvelheroes.data.api

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "squad"
)
data class Result(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val description: String? = null,
    val name: String? = null,
    val thumbnail: Thumbnail? = null,
) : Serializable