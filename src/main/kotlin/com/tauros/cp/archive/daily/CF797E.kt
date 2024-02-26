package com.tauros.cp.archive.daily

import com.tauros.cp.FastReader
import com.tauros.cp.FastWriter
import com.tauros.cp.ar
import com.tauros.cp.common.sqrt
import com.tauros.cp.iar

/**
 * @author tauros
 * 2023/12/1
 */
private val bufCap = 65536
private val rd = FastReader(System.`in`, bufCap)
private val wt = FastWriter(System.out, bufCap)
private val INF = 0x3f3f3f3f
private val INF_LONG = 0x3f3f3f3f3f3f3f3fL

private fun solve() {
    val n = rd.ni()
    val nums = rd.na(n)
    val sq = sqrt(n)
    val dp = ar(sq + 1) { iar(n + 1) }
    for (k in 1 .. sq) {
        for (i in n - 1 downTo 0) {
            val next = minOf(n, i + nums[i] + k)
            dp[k][i] = dp[k][next] + 1
        }
    }
    val q = rd.ni()
    repeat(q) {
        val (p, k) = rd.ni() - 1 to rd.ni()
        if (k <= sq) {
            wt.println(dp[k][p])
        } else {
            var (iter, ans) = p to 0
            while (iter < n) {
                ans++
                iter += nums[iter] + k
            }
            wt.println(ans)
        }
    }
}

fun main(args: Array<String>) {
    solve()
    wt.flush()
}