package com.tauros.cp.geometry

import com.tauros.cp.common.sqrt


/**
 * @author tauros
 */
// int
data class IPoint2(val x: Int, val y: Int) {
    fun toVector() = IVector2(x, y)
    operator fun plus(other: IVector2) = IPoint2(x + other.x, y + other.y)
    operator fun minus(other: IVector2) = IPoint2(x - other.x, y - other.y)
    operator fun minus(other: IPoint2) = IVector2(x - other.x, y - other.y)
    override fun equals(other: Any?): Boolean {
        return if (other === this) true
        else if (other !is IPoint2) false
        else x == other.x && y == other.y
    }
    override fun hashCode() = x.hashCode() xor y.hashCode()
}

data class IVector2(val x: Int, val y: Int) {
    fun len2() = x.toLong() * x + y.toLong() * y
    fun len() = sqrt(len2())
    operator fun plus(other: IVector2) = IVector2(x + other.x, y + other.y)
    operator fun minus(other: IVector2) = IVector2(x - other.x, y - other.y)
    // cos
    infix fun dot(other: IVector2) = x.toLong() * other.x + y.toLong() * other.y
    // sin
    infix fun cross(other: IVector2) = x.toLong() * other.y - other.x * y.toLong()
    infix fun isParallel(other: IVector2) = (this cross other) == 0L
    infix fun isPerpendicular(other: IVector2) = (this dot other) == 0L
    infix fun side(other: IVector2) = (this cross other).let { if (it < 0) -1 else if (it > 0) 1 else 0 }
    override fun equals(other: Any?): Boolean {
        return if (other === this) true
        else if (other !is IVector2) false
        else x == other.x && y == other.y
    }
    override fun hashCode() = x.hashCode() xor y.hashCode()
}

// long
data class LPoint2(val x: Long, val y: Long) {
    fun toVector() = LVector2(x, y)
    operator fun plus(other: LVector2) = LPoint2(x + other.x, y + other.y)
    operator fun minus(other: LVector2) = LPoint2(x - other.x, y - other.y)
    operator fun minus(other: LPoint2) = LVector2(x - other.x, y - other.y)
    override fun equals(other: Any?): Boolean {
        return if (other === this) true
        else if (other !is LPoint2) false
        else x == other.x && y == other.y
    }
    override fun hashCode() = x.hashCode() xor y.hashCode()
}

fun IPoint2.toLPoint2() = LPoint2(this.x.toLong(), this.y.toLong())

data class LVector2(val x: Long, val y: Long) {
    fun len2() = x * x + y * y
    fun len() = sqrt(len2())
    operator fun plus(other: LVector2) = LVector2(x + other.x, y + other.y)
    operator fun minus(other: LVector2) = LVector2(x - other.x, y - other.y)
    // cos
    infix fun dot(other: LVector2) = x * other.x + y * other.y
    // sin
    infix fun cross(other: LVector2) = x * other.y - other.x * y
    infix fun isParallel(other: LVector2) = (this cross other) == 0L
    infix fun isPerpendicular(other: LVector2) = (this dot other) == 0L
    infix fun side(other: LVector2) = (this cross other).let { if (it < 0) -1 else if (it > 0) 1 else 0 }
    override fun equals(other: Any?): Boolean {
        return if (other === this) true
        else if (other !is LVector2) false
        else x == other.x && y == other.y
    }
    override fun hashCode() = x.hashCode() xor y.hashCode()
}

fun IVector2.toLVector2() = LVector2(this.x.toLong(), this.y.toLong())
