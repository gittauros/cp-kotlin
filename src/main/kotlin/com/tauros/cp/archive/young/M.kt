package com.tauros.cp.archive.young

import com.tauros.cp.common.boolean
import com.tauros.cp.common.double
import com.tauros.cp.common.int
import com.tauros.cp.common.string
import com.tauros.cp.common.ternarySearch
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * @author tauros
 * 2025/3/4
 */
fun main(args: Array<String>) {
    operator fun List<double>.component6() = this[5]
    operator fun List<double>.component7() = this[6]
    val nums = readLine()!!.split(" ").map { it.toDouble() }
    val (n, r, h, x0, y0, vx, vy) = nums

    fun intersect(): Pair<double, double> {
        // (x0 + t*vx)^2 + (y0 + t*vy)^2 = r^2
        val a = vx * vx + vy * vy
        val b = 2 * x0 * vx + 2 * y0 * vy
        val c = x0 * x0 + y0 * y0 - r * r
        val delta = sqrt(b * b - 4 * a * c)
        return (-b - delta) / (2 * a) to (-b + delta) / (2 * a)
    }
    val (t1, t2) = intersect()

    val t = t2
    val chordTime = t2 - t1
    val low = h / (t + n * chordTime)
    val high = h / (t + (n - 1) * chordTime)
    println(string.format("%.8f %.8f\n", low, high))
}