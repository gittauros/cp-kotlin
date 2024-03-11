package com.tauros.cp.archive.ternary

import com.tauros.cp.common.string
import com.tauros.cp.common.ternarySearch
import kotlin.math.abs

/**
 * @author tauros
 * 2024/3/11
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/780/B
    // 练习下黄金分割三分法
    val n = readln().toInt()
    val positions = readln().split(" ").map(string::toInt).toIntArray()
    val velocities = readln().split(" ").map(string::toInt).toIntArray()

    val (l, r) = positions.min().toDouble() to positions.max().toDouble()
    val (_, ans) = ternarySearch(l, r, { a, b -> a.second < b.second }) { location ->
        (0 until n).maxOf { abs(positions[it] - location) / velocities[it] }
    }
    println(string.format("%.8f", ans))
}