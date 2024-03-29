package com.tauros.cp.archive.daily

import com.tauros.cp.common.string

/**
 * @author tauros
 * 2024/3/27
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/924/B
    val (n, u) = readln().split(" ").map { it.toInt() }
    val e = readln().split(" ").map { it.toInt() }.toIntArray()

    var ans = -1.0
    var r = 1
    for (l in 0 .. n - 3) {
        while (r < n && e[r] - e[l] <= u) r++
        if (r - l >= 3) {
            ans = maxOf((e[r - 1] - e[l + 1].toDouble()) / (e[r - 1] - e[l]), ans)
        }
    }
    if (ans < 0) println(-1)
    else println(string.format("%.12f", ans))
}