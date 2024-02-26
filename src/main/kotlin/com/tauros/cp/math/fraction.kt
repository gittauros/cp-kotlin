package com.tauros.cp.math

import com.tauros.cp.common.gcd


/**
 * @author tauros
 * 2023/9/11
 */
// b/a
class Frac(b: Int, a: Int) : Comparable<Frac> {
    companion object {
        val ZERO = Frac(0, 1)
        val ONE = Frac(1, 1)
        val INF = Frac(Int.MAX_VALUE / 2, 1)
    }
    val b = b / gcd(a, b)
    val a = a / gcd(a, b)

    operator fun plus(other: Frac): Frac {
        val lcm = a / gcd(a, other.a) * other.a
        return Frac(lcm / a * b + lcm / other.a * other.b, lcm)
    }

    operator fun minus(other: Frac): Frac {
        val lcm = a / gcd(a, other.a) * other.a
        return Frac(lcm / a * b - lcm / other.a * other.b, lcm)
    }

    operator fun times(other: Frac): Frac {
        return Frac(b * other.b, a * other.a)
    }

    operator fun div(other: Frac): Frac {
        return Frac(b * other.a, a * other.b)
    }

    override fun compareTo(other: Frac): Int {
        val diff = this - other
        return if (diff.b == 0) 0 else if (diff.b > 0) diff.a.compareTo(0) else -diff.a.compareTo(0)
    }

    override fun toString(): String {
        return buildString {
            append(b)
            append('/')
            append(a)
        }
    }

    fun toDouble() = b.toDouble() / a

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Frac
        return this.compareTo(other) == 0
    }

    override fun hashCode(): Int {
        var result = b
        result = 31 * result + a
        return result
    }

    fun toFracLong() = FracLong(b.toLong(), a.toLong())
}

fun Int.toFrac() = Frac(this, 1)

class FracLong(b: Long, a: Long) : Comparable<FracLong> {
    companion object {
        val ZERO = FracLong(0, 1)
        val ONE = FracLong(1, 1)
        val INF = FracLong(Long.MAX_VALUE / 2, 1)
    }
    val b = b / gcd(a, b)
    val a = a / gcd(a, b)

    operator fun plus(other: FracLong): FracLong {
        val lcm = a / gcd(a, other.a) * other.a
        return FracLong(lcm / a * b + lcm / other.a * other.b, lcm)
    }

    operator fun minus(other: FracLong): FracLong {
        val lcm = a / gcd(a, other.a) * other.a
        return FracLong(lcm / a * b - lcm / other.a * other.b, lcm)
    }

    operator fun times(other: FracLong): FracLong {
        return FracLong(b * other.b, a * other.a)
    }

    operator fun div(other: FracLong): FracLong {
        return FracLong(b * other.a, a * other.b)
    }

    override fun compareTo(other: FracLong): Int {
        val diff = this - other
        return if (diff.b == 0L) 0 else if (diff.b > 0) diff.a.compareTo(0) else -diff.a.compareTo(0)
    }

    override fun toString(): String {
        return buildString {
            append(b)
            append('/')
            append(a)
        }
    }

    fun toDouble() = b.toDouble() / a

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FracLong
        return this.compareTo(other) == 0
    }

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + a.hashCode()
        return result
    }
}

fun Int.toFracLong() = FracLong(this.toLong(), 1)
fun Long.toFracLong() = FracLong(this, 1)