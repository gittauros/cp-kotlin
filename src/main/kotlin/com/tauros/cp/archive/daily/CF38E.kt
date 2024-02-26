package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.lar

/**
 * @author tauros
 * 2023/12/3
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val marbles = ar(n) { rd.na(2) }
    marbles.sortBy { it[0] }
    val dp = lar(n + 1)
    for (i in n - 1 downTo 0) {
        dp[i] = INF_LONG
        var sum = 0L
        for (j in i + 1 .. n) {
            sum += marbles[j - 1][0] - marbles[i][0]
            dp[i] = minOf(dp[i], dp[j] + sum + marbles[i][1])
        }
    }
    wt.println(dp[0])
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}