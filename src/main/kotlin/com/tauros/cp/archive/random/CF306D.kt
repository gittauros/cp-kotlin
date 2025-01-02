package com.tauros.cp.archive.random

import com.tauros.cp.common.string
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * @author tauros
 * 2025/1/2
 */
fun main(args: Array<String>) {
    // 随机化，精度很痛苦
    val n = readln().toInt()
    if (n <= 4) {
        println("No solution")
    } else {
        val rotate = Math.PI * 2 / n
        var (angle, d) = rotate to 500.0

        var (x, y) = 0.0 to 0.0
        val ans = mutableListOf(x to y)
        repeat(n - 2) {
            x += d * cos(angle)
            y += d * sin(angle)
            ans.add(x to y)
            angle += rotate
            d += 1e-2
        }
        ans.add(x - y / tan(angle) to 0.0)
        print(buildString {
            for ((px, py) in ans) {
                append(string.format("%.3f %.3f\n", px, py))
            }
        })
    }
}