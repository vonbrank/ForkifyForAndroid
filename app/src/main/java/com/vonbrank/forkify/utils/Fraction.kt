package com.vonbrank.forkify.utils

import kotlin.math.abs
import kotlin.math.floor

class Fraction(private val number: Double, private val mixedNumber: Boolean = true) {

    companion object {
        private fun convertDecimalToFraction(x: Double): String {

            if (isInt(x)) return "${x.toInt()}"

            if (x < 0) {
                return "-" + convertDecimalToFraction(-x)
            }
            val tolerance = 1.0E-6
            var h1 = 1.0
            var h2 = 0.0
            var k1 = 0.0
            var k2 = 1.0
            var b = x
            do {
                val a = floor(b)
                var aux = h1
                h1 = a * h1 + h2
                h2 = aux
                aux = k1
                k1 = a * k1 + k2
                k2 = aux
                b = 1 / (b - a)
            } while (abs(x - h1 / k1) > x * tolerance)
            return "${h1.toInt()}/${k1.toInt()}"
        }

        private fun isInt(x: Double): Boolean {
            return x == x.toInt().toDouble()
        }

    }


    private fun isInt(): Boolean {
        return Companion.isInt(number)
    }

    override fun toString(): String {

        if (abs(number) <= 1) return convertDecimalToFraction(number)

        if (mixedNumber) {
            if (isInt()) return "${number.toInt()}"

            val intPart = number.toInt()
            val decimalPart = number - intPart
            return "$intPart ${convertDecimalToFraction(decimalPart)}"
        }
        return convertDecimalToFraction(number)
    }
}