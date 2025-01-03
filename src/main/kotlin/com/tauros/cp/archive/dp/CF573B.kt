package com.tauros.cp.archive.dp

import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/4/27
 */
fun main(args: Array<String>) {
    // https://codeforces.com/problemset/problem/573/B
    // 没想到做过一次还是不会，学的羊解
    val n = readln().toInt()
    val heights = readln().split(" ").map { it.toInt() }.toIntArray()

    val ans = iar(n) { heights[it] }
    ans[0] = minOf(ans[0], 1)
    for (i in 1 until n) ans[i] = minOf(ans[i], ans[i - 1] + 1)
    ans[n - 1] = minOf(ans[n - 1], 1)
    for (i in n - 2 downTo 0) ans[i] = minOf(ans[i], ans[i + 1] + 1)

    println(ans.max())
}