package com.zavierdev.cinemacatalogue.utils

class RandomGenerator {
    fun generateString(length: Int): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun generateInteger(valueFrom: Int, valueTo: Int): Int {
        return (valueFrom..valueTo).random()
    }
}