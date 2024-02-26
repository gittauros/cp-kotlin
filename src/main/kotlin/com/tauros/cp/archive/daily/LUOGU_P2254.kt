@file:Suppress("NOTHING_TO_INLINE", "EXPERIMENTAL_FEATURE_WARNING", "OVERRIDE_BY_INLINE", "DEPRECATION")
@file:OptIn(ExperimentalStdlibApi::class)

package com.tauros.cp.archive.daily


import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ao
import com.tauros.cp.ar
import com.tauros.cp.bar
import com.tauros.cp.iao
import com.tauros.cp.iar
import kotlin.math.abs

/**
 * @author tauros
 * 2023/10/19
 */
private val bufCap = 128

//private val rd = FastReader(FileInputStream("/Users/tauros/Downloads/"), bufCap)
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val (n, m, x, y, k) = rd.na(5)
    val grid = ar(n) { bar(m) }
    for (i in 0 until n) {
        val str = rd.ns(m)
        for (j in 0 until m) grid[i][j] = str[j] == '.'
    }
    val ranges = ar(k) { iao(rd.ni(), rd.ni(), rd.ni() - 1) }
    val ops = ao(iao(-1, 0), iao(1, 0), iao(0, -1), iao(0, 1))
    val queue = ar(2) { iar(maxOf(n, m) + 1) }
    val dp = ar(2) { ar(n) { iar(m) { -INF } } }
    var cur = 0
    dp[cur][x - 1][y - 1] = 0
    for ((st, ed, op) in ranges) {
        val pre = cur
        cur = cur xor 1
        dp[cur].forEach { it.fill(-INF) }
        val (di, dj) = ops[op]
        val len = ed - st + 1
        if (op < 2) {
            for (j in 0 until m) {
                var iter = if (op and 1 == 0) n - 1 else 0
                var (head, tail) = 0 to 0
                var far = 0
                while (iter in 0 until n) {
                    if (grid[iter][j]) far++ else far = 0
                    while (head < tail && abs(iter - queue[1][head]) > minOf(far, len)) head++
                    while (head < tail && queue[0][tail - 1] < dp[pre][iter][j] - di * iter) tail--
                    queue[0][tail] = dp[pre][iter][j] - di * iter
                    queue[1][tail++] = iter
                    if (grid[iter][j]) dp[cur][iter][j] = queue[0][head] + di * iter
                    iter += di
                }
            }
        } else {
            for (i in 0 until n) {
                var iter = if (op and 1 == 0) m - 1 else 0
                var (head, tail) = 0 to 0
                var far = 0
                while (iter in 0 until m) {
                    if (grid[i][iter]) far++ else far = 0
                    while (head < tail && abs(iter - queue[1][head]) > minOf(far, len)) head++
                    while (head < tail && queue[0][tail - 1] < dp[pre][i][iter] - dj * iter) tail--
                    queue[0][tail] = dp[pre][i][iter] - dj * iter
                    queue[1][tail++] = iter
                    if (grid[i][iter]) dp[cur][i][iter] = queue[0][head] + dj * iter
                    iter += dj
                }
            }
        }
    }
    val ans = dp[cur].map { it.reduce(::maxOf) }.reduce(::maxOf)
    wt.println(ans)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}