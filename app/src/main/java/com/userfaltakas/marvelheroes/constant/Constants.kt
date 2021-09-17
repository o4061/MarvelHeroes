package com.userfaltakas.marvelheroes.constant

import java.math.BigInteger
import java.security.MessageDigest
import java.sql.Timestamp

object Constants {
    const val API_KEY = "6bec36c935c9009b9c655efa7dd49635"
    const val PRIVATE_KEY = "46eeb6faf3efb862ea34afa19b3b4658dcfa0e37"
    const val BASE_URL = "https://gateway.marvel.com/v1/public/"
    val ts = Timestamp(System.currentTimeMillis()).time.toString()

    fun hash(): String {
        val input = "$ts$PRIVATE_KEY$API_KEY"
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}