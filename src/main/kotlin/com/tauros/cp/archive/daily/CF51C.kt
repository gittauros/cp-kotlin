package com.tauros.cp.archive.daily

import com.tauros.cp.common.findFirst
import com.tauros.cp.common.string
import com.tauros.cp.dar

/**
 * @author tauros
 * 2025/1/2
 */
fun main(args: Array<String>) {
    val n = readln().toInt()
    val coords = readln().split(" ").map { it.toInt() }.sorted()
    val (max, min) = coords.last().toDouble() to coords.first().toDouble()

    var (l, r) = 0.0 to max - min
    var ans = dar(3)
    repeat(50) {
        val mid = (l + r) / 2

        val stations = dar(3)
        var pivot = 0
        repeat(3) {
            if (pivot >= n) {
                stations[it] = stations[it - 1]
                return@repeat
            }
            val pos = coords[pivot] + mid * 2
            stations[it] = coords[pivot] + mid
            pivot = findFirst(n) { coords[it] > pos }
        }
        if (pivot >= n) {
            ans = stations
            r = mid
        } else {
            l = mid
        }
    }
    println(string.format("%.6f", (l + r) / 2))
    println(buildString { for (res in ans) append(string.format("%.6f ", res)) })
}