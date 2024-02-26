package com.tauros.cp.archive.math

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.iar

/**
 * @author tauros
 * 2024/2/19
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    // https://codeforces.com/problemset/problem/1237/E
    val n = rd.ni()
//    val dp = iar(n + 10)
//    dp[1] = 1; dp[2] = 1
//
//    for (i in 2 .. n step 2) if (dp[i] > 0) {
//        val h = i.takeHighestOneBit().countTrailingZeroBits()
//        for (j in (1 shl h) - 1 until (1 shl h + 1)) if (i + j + 1 <= n && dp[j] > 0) {
//            dp[i + j + 1] = dp[i] * dp[j]
//        }
//    }
//    wt.println(dp[n])
    var iter = 1
    while (iter + 1 < n) {
        iter += iter + iter % 2 + 1
    }
    wt.println(if (iter == n || iter + 1 == n) 1 else 0)
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}