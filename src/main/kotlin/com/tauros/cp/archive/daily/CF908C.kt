package com.tauros.cp.archive.daily

import com.tauros.cp.common.double
import com.tauros.cp.common.string
import com.tauros.cp.mlo
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * @author tauros
 * 2024/3/23
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/908/C
    // 找和之前圆心距离为2r时最大的y即可
    val (n, r) = readln().split(" ").map(string::toInt)
    val xs = readln().split(" ").map(string::toInt).toIntArray()

    val ans = buildString {
        val centers = mlo<Pair<double, double>>()
        for (x in xs) {
            var res = r.toDouble()
            for ((x1, y1) in centers) if (abs(x - x1) <= 2 * r) {
                val len2 = r.toDouble() * r * 4 - (x.toDouble() - x1) * (x.toDouble() - x1)
                val y = y1 + sqrt(len2)
                res = maxOf(res, y)
            }
            append(string.format("%.8f ", res))
            centers.add(x.toDouble() to res)
        }
    }
    println(ans)
}